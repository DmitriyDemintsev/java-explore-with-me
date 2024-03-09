package ru.practicum.service.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.dto.HitDto;
import ru.practicum.enums.EventState;
import ru.practicum.enums.StateAction;
import ru.practicum.exception.*;
import ru.practicum.model.*;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.repository.query.EventPredicatesBuilder;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.request.RequestService;
import ru.practicum.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final RequestService requestService;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;

    @Override
    public Event create(Long userId, Event event, Long categoryId) {
        User initiator = userService.getUserById(userId);
        Category category = categoryService.getCategoryById(categoryId);
        if (event.getAnnotation() == null || event.getAnnotation().isBlank()) {
            throw new EventValidationException("Отсутствует аннотация события");
        }
        if (event.getDescription() == null || event.getDescription().isBlank()) {
            throw new EventValidationException("Отсутствует описание события");
        }
        if (event.getEventDate() == null || event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventValidationException("Дата проведения мероприятия не указана " +
                    "или составляет менее 2 часов от момента создания события");
        }
        if (event.getTitle() == null || event.getTitle().isEmpty()) {
            throw new EventValidationException("Отсутствует заголовок для события");
        }
        if (event.getLocation() == null) {
            throw new EventValidationException("Укажите место проведения мероприятия");
        }
        if (event.getPaid() == null) {
            throw new EventValidationException("Укажите, является мероприятие платным или нет");
        }
        if (event.getParticipantLimit() < 0) {
            throw new EventValidationException("Заявленное количество участников не может быть меньше нуля");
        }
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setEventState(EventState.PENDING);
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long userId, Long categoryId, Event event, StateAction stateAction) {
        Event old = eventRepository.findById(event.getId())
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));
        User initiator = Optional.ofNullable(userId)
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("Пользователь не найден")))
                .orElse(old.getInitiator());
        event.setInitiator(initiator);
        calcState(stateAction, event);
        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            event.setCategory(category);
        }
        if (event.getAnnotation() == null) {
            event.setAnnotation(old.getAnnotation());
        }
        if (event.getCategory() == null) {
            event.setCategory(old.getCategory());
        }
        if (event.getDescription() == null) {
            event.setDescription(old.getDescription());
        }
        if (event.getEventDate() == null) {
            event.setEventDate(old.getEventDate());
        }
        checkEventTimes(event);
        if (event.getLocation() == null) {
            event.setLocation(old.getLocation());
        }
        if (event.getPaid() == null) {
            event.setPaid(old.getPaid());
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(old.getParticipantLimit());
        }
        if (event.getParticipantLimit() < 0) {
            throw new EventValidationException("Количество участников не может быть отрицательным числом");
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(old.getRequestModeration());
        }
        if (event.getEventState() == null) {
            event.setEventState(old.getEventState());
        }
        if (old.getEventState() == EventState.PUBLISHED) {
            throw new EventAlreadyExistException("Событие уже опубликовано");
        }
        if (old.getEventState() == EventState.CANCELED && stateAction == StateAction.PUBLISH_EVENT) {
            throw new EventAlreadyExistException("Событие уже опубликовано");
        }
        if (event.getTitle() == null) {
            event.setTitle(old.getTitle());
        }
        if (event.getAnnotation().isEmpty()) {
            throw new EventValidationException("Отсутствует аннотация");
        }
        if (event.getDescription().isEmpty()) {
            throw new EventValidationException("Отсутствует описание");
        }
        if (event.getTitle().isEmpty()) {
            throw new EventValidationException("Отсутствует заголовок");
        }
        return eventRepository.save(event);
    }

    @Override
    public Event getEventById(Long userId, Long eventId, String ip) {
        Optional.ofNullable(userId)
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("Пользователь не найден")))
                .orElse(null);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));
        if ((event.getEventState() != EventState.PUBLISHED) && !event.getInitiator().getId().equals(userId)) {
            throw new EventNotFoundException("Событие не найдено");
        }
        enrichEvent(event);
        return event;
    }

    @Override
    public List<Event> getListsEvents(Long userId, List<Long> userIds, List<String> stateCodes, String text,
                                      List<Long> categoriesId, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                                      Integer from, Integer size) {
        if (rangeStart != null && rangeEnd != null && !rangeEnd.isAfter(rangeStart)) {
            throw new EventValidationException("rangeStart обязан быть раньше rangeEnd");
        }
        User user = Optional.ofNullable(userId)
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new UserNotFoundException("Пользователь не найден")))
                .orElse(null);
        List<User> users = userIds != null ? userRepository.findAllById(userIds) : null;
        List<EventState> states = stateCodes != null ? stateCodes.stream().map(EventState::valueOf)
                .collect(Collectors.toList()) : null;
        List<Category> categories = categoriesId != null ? categoryRepository.findAllByIdIn(categoriesId) : null;
        EventPredicatesBuilder builder = new EventPredicatesBuilder();
        Optional.ofNullable(user).ifPresent(it -> builder.with("initiator", "=", it));
        Optional.ofNullable(users).ifPresent(it -> builder.with("initiator", "in", it));
        Optional.ofNullable(states).ifPresent(it -> builder.with("eventState", "in", it));

        Optional.ofNullable(categories).ifPresent(it -> builder.with("category", "in", it));
        Optional.ofNullable(paid).ifPresent(it -> builder.with("paid", "=", it));
        Optional.ofNullable(rangeStart).ifPresent(it -> builder.with("eventDate", ">=", it));
        Optional.ofNullable(rangeEnd).ifPresent(it -> builder.with("eventDate", "<=", it));
        Optional.ofNullable(onlyAvailable).ifPresent(it -> builder.with("requestStatus", "<",
                new PathBuilder<>(Event.class, "event").getNumber("participantLimit", Integer.class)));
        BooleanExpression expression = builder.build();

        if (text != null) {
            PathBuilder<Event> entityPath = new PathBuilder<>(Event.class, "event");
            StringPath annotation = entityPath.getString("annotation");
            StringPath description = entityPath.getString("description");

            BooleanExpression or = annotation.likeIgnoreCase(text)
                    .or(description.likeIgnoreCase(text));

            expression = expression.and(or);
        }

        List<Event> events = eventRepository.findAll(expression, getPageable(from, size, sort)).getContent();
        for (Event event : events) {
            enrichEvent(event);
        }
        return events;
    }

    public static Pageable getPageable(int from, int size, String sortParam) {
        Sort sort;
        if (sortParam == null) {
            sort = Sort.by("eventDate");
        } else {
            switch (sortParam) {
                case "EVENT_DATE":
                    sort = Sort.by("eventDate");
                    break;
                default:
                    throw new IllegalArgumentException("Неподдерживаемый формат сортировки: " + sortParam);
            }
        }
        return PageRequest.of(from > 0 ? from / size : 0, size, sort);
    }

    @Override
    public RequestsByStatus updateRequestStatusEvent(Long userId, Long eventId,
                                                     RequestStatusUpdate request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RequestNotFoundException("Запрос на участие не найден"));
        return requestService.updateRequestsStatusByEvent(request, event);
    }

    @Override
    public List<Request> getRequestsByEventId(Long userId, Long eventId) {
        return requestService.getRequestsByEventId(eventId);
    }

    @Override
    public List<Event> findByIds(List<Long> eventsId) {
        return eventRepository.findAllById(eventsId);
    }

    public void enrichEvent(Event event) {
        event.setViews(loadViews(event));
    }

    private int loadViews(Event event) {
        List<String> uris = List.of("/events/" + event.getId());
        List<HitDto> hits = statsClient.getStats(LocalDateTime.now().minusYears(25),
                LocalDateTime.now().plusYears(80),
                uris, true);
        return hits.stream().mapToInt(HitDto::getHits).sum();
    }

    private void calcState(StateAction stateAction, Event event) {
        if (stateAction == null) {
            return;
        }
        switch (stateAction) {
            case PUBLISH_EVENT:
                event.setEventState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
                break;
            case REJECT_EVENT:
                event.setEventState(EventState.CANCELED);
                break;
            case SEND_TO_REVIEW:
                event.setEventState(EventState.PENDING);
                break;
            case CANCEL_REVIEW:
                event.setEventState(EventState.CANCELED);
                break;
        }
    }

    private void checkEventTimes(Event event) {
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventValidationException("Дата предстоящего события на может быть в настоящем");
        }
        if (Objects.nonNull(event.getPublishedOn())) {
            if (event.getEventDate().isBefore(event.getPublishedOn().plusHours(1))) {
                throw new EventValidationException("Дата предстоящего события на может быть в настоящем");
            }
        }
    }
}

package ru.practicum.service.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.dto.HitDto;
import ru.practicum.enums.EventState;
import ru.practicum.enums.RequestStatus;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.EventValidationException;
import ru.practicum.exception.RequestNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.model.*;
import ru.practicum.repository.*;
import ru.practicum.repository.query.EventPredicatesBuilder;
import ru.practicum.service.category.CategoryService;
import ru.practicum.service.request.RequestService;
import ru.practicum.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;

    @Override
    public Event create(Long userId, Event event) {
        User initiator = userService.getUserById(userId);
        Category category = categoryService.getCategoryById(event.getCategory().getId());
        if (event.getAnnotation() == null || event.getAnnotation().isEmpty()) {
            throw new EventValidationException("Отсутствует аннотация события");
        }
        if (event.getDescription() == null || event.getDescription().isEmpty()) {
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
    public Event updateEvent(Long userId, Long eventId, Event event) {
        Event old = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));
        userRepository.findById(old.getInitiator().getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        if (!old.getInitiator().getId().equals(userId)) {
            throw new EventNotFoundException("Данная операция для вас недоступна");
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
        if (event.getLocation() == null) {
            event.setLocation(old.getLocation());
        }
        if (event.getPaid() == null) {
            event.setPaid(old.getPaid());
        }
        if (event.getParticipantLimit() == null) {
            event.setParticipantLimit(old.getParticipantLimit());
        }
        if (event.getRequestModeration() == null) {
            event.setRequestModeration(old.getRequestModeration());
        }
        if (event.getEventState() == null) {
            event.setEventState(old.getEventState());
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
    public Event getEventById(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));
        List<HitDto> hits = statsClient.getStats("/event/" + eventId);
        Integer views = hits.stream().mapToInt(HitDto::getHits).sum();
        event.setViews(views);
        event.setConfirmedRequests(requestRepository.countByEventAndStatusRequest(event,
                RequestStatus.CONFIRMED));
        //     event.setComments(commentRepository.countByEvent(event));
        return event;
    }

    @Override
    public List<Event> getListsEvents(Long userId, Long eventId, List<Long> users, List<String> states, String text,
                                      List<Long> categoriesIds, Boolean paid, String rangeStart,
                                      String rangeEnd, Boolean onlyAvailable, String sort,
                                      Integer from, Integer size) {
        List<Category> categories = categoryRepository.findAllById(categoriesIds);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));
        EventPredicatesBuilder builder = new EventPredicatesBuilder();
        Optional.ofNullable(user).ifPresent(it -> builder.with("user", "=", it));
        Optional.ofNullable(event).ifPresent(it -> builder.with("event", "=", it));

        Optional.of(users).ifPresent(it -> builder.with("users", "in", it));
        Optional.of(states).ifPresent(it -> builder.with("states", "in", it));

        Optional.ofNullable(text).ifPresent(it -> builder.with("text", "~", it));
        Optional.of(categories).ifPresent(it -> builder.with("category", "in", it));
        Optional.ofNullable(paid).ifPresent(it -> builder.with("paid", "=", it));
        Optional.ofNullable(rangeStart).ifPresent(it -> builder.with("eventDate", ">=", it));
        Optional.ofNullable(rangeEnd).ifPresent(it -> builder.with("eventDate", "<=", it));
        Optional.ofNullable(onlyAvailable).ifPresent(it -> builder.with("onlyAvailable", "=", it));
        BooleanExpression expression = builder.build();

        return eventRepository.findAll(expression, getPageable(from, size, sort)).getContent();
    }

    @Override
    public RequestsByStatus updateRequestStatusEvent(Long userId, Long eventId,
                                                     RequestStatusUpdate requestStatusUpdate) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RequestNotFoundException("Запрос на участие не найден"));
        return requestService.updateRequestsStatusByEvent(requestStatusUpdate, event);
    }

    @Override
    public List<Request> getRequestsByEventId(Long userId, Long eventId) {
        return requestService.getRequestsByEventId(eventId);
    }



    public static Pageable getPageable(int from, int size, String sort) {
        return PageRequest.of(from > 0 ? from / size : 0, size, Sort.Direction.valueOf(sort));
    }
}

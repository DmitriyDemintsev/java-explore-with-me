package ru.practicum.service.event;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enums.StateAction;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.RequestStatusUpdate;
import ru.practicum.model.RequestsByStatus;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface EventService {

    @Transactional
    Event create(Long userId, Event event, Long categoryId);

    @Transactional
    Event updateEvent(Long userId, Long categoryId, Event event, StateAction stateAction);

    Event getEventById(Long userId, Long eventId, String ip);

    List<Event> getListsEvents(Long userId, List<Long> users, List<String> states, String text,
                               List<Long> categoriesIds, Boolean paid, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                               Integer from, Integer size);

    @Transactional
    RequestsByStatus updateRequestStatusEvent(Long userId, Long eventId, RequestStatusUpdate requestStatusUpdate);

    List<Request> getRequestsByEventId(Long userId, Long eventId);

    List<Event> findByIds(List<Long> eventsId);

    void enrichEvent(Event event);
}

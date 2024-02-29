package ru.practicum.service.event;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.RequestStatusUpdate;
import ru.practicum.model.RequestsByStatus;

import java.util.List;

@Transactional(readOnly = true)
public interface EventService {

    @Transactional
    Event create(Long userId, Event event);

    @Transactional
    Event updateEvent(Long userId, Long eventId, Event event);

    Event getEventById(Long userId, Long eventId);

    List<Event> getListsEvents(Long userId, Long eventId, List<Long> users, List<String> states, String text,
                               List<Long> categoriesIds, Boolean paid, String rangeStart,
                               String rangeEnd, Boolean onlyAvailable, String sort,
                               Integer from, Integer size);


    RequestsByStatus updateRequestStatusEvent(Long userId, Long eventId, RequestStatusUpdate requestStatusUpdate);

    List<Request> getRequestsByEventId(Long userId, Long eventId);
}

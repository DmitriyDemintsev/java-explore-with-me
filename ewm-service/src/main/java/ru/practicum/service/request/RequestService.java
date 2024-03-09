package ru.practicum.service.request;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.RequestStatusUpdate;
import ru.practicum.model.RequestsByStatus;

import java.util.List;

@Transactional(readOnly = true)
public interface RequestService {

    @Transactional
    Request create(Long userId, Long eventId);

    Request cansel(Long userId, Long requestId);

    List<Request> getRequestsByUserId(Long userId);

    List<Request> getRequestsByEventId(Long eventId);

    RequestsByStatus updateRequestsStatusByEvent(RequestStatusUpdate statusUpdate, Event event);
}

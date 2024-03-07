package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enums.EventState;
import ru.practicum.enums.RequestStatus;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.RequestAlreadyExistException;
import ru.practicum.exception.RequestNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.model.*;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    /**
     * создаем заявку на участие в событии
     */
    @Override
    public Request create(Long userId, Long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));
        checkRequest(requester, event);
        Request request = new Request(
                null,
                LocalDateTime.now(),
                event,
                requester,
                event.getParticipantLimit() == 0 ? RequestStatus.CONFIRMED : RequestStatus.PENDING
        );
        return requestRepository.save(request);
    }

    /**
     * отменяем свое участие в событии
     */
    @Override
    public Request cansel(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Запрос на участие не найден"));
        request.setRequestStatus(RequestStatus.CANCELED);
        return requestRepository.save(request);

    }

    /**
     * смотрим, на участие в каких событиях заявились
     */
    @Override
    public List<Request> getRequestsByUserId(Long userId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        List<Request> requests = requestRepository.findAllByRequester(requester);
        return requests;
    }

    @Override
    public List<Request> getRequestsByEventId(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));
        List<Request> requests = requestRepository.findAllByEvent(event);
        return requests;
    }

    @Override
    @Transactional
    public RequestsByStatus updateRequestsStatusByEvent(RequestStatusUpdate statusUpdate, Event event) {
        int space = getAvailableSpace(event);
        RequestStatus requestStatus = RequestStatus.valueOf(statusUpdate.getStatus().toString());
        List<Long> requestIds = statusUpdate.getRequestIds();
        List<Request> requests = requestRepository.findAllById(requestIds);
        requests = modifyStatusRequests(requests, space, requestStatus);
        return toRequestsByStatus(requests);
    }

    public Integer getAvailableSpace(Event event) {
        Integer limit = event.getParticipantLimit();
        if (limit == 0) {
            throw new RequestAlreadyExistException("Собыьие не имеет ограничений на количество участников");
        }
        List<Request> confirmedRequest = requestRepository.getRequestByStatusIs(RequestStatus.CONFIRMED, event);
        int availableSpace = limit - confirmedRequest.size();
        if (availableSpace == 0) {
            throw new RequestAlreadyExistException("Исчерпан лимит на количество участников");
        }
        return availableSpace;
    }

    private List<Request> modifyStatusRequests(List<Request> requests, int space, RequestStatus requestStatus) {
        final List<Request> result = new ArrayList<>();
        for (Request request : requests) {
            Request update = new Request(
                    request.getId(),
                    request.getCreated(),
                    request.getEvent(),
                    request.getRequester(),
                    request.getRequestStatus()
            );
            if (!request.getRequestStatus().equals(RequestStatus.PENDING)) {
                throw new RequestAlreadyExistException("Некорректный лимит участников");
            }
            if (space > 0) {
                update.setRequestStatus(requestStatus);
                if (Objects.equals(requestStatus, RequestStatus.CONFIRMED)) {
                    space = space - 1;
                }
            } else {
                update.setRequestStatus(RequestStatus.REJECTED);
            }
            result.add(requestRepository.save(update));
        }
        return new ArrayList<>(result);
    }

    public static RequestsByStatus toRequestsByStatus(List<Request> requests) {
        List<Request> confirmedRequests = new ArrayList<>();
        List<Request> rejectedRequests = new ArrayList<>();
        for (Request request : requests) {
            if (Objects.equals(request.getRequestStatus(), RequestStatus.CONFIRMED)) {
                confirmedRequests.add(request);
            } else {
                rejectedRequests.add(request);
            }
        }
        return new RequestsByStatus(confirmedRequests, rejectedRequests);

    }

    private void checkRequest(User requester, Event event) {
        if (event.getInitiator().getId() == requester.getId()) {
            throw new RequestAlreadyExistException("Вы являетесь организатором события, запрос на участие не требуется");
        }
        if (event.getEventState() != EventState.PUBLISHED) {
            throw new RequestAlreadyExistException("Нет такого события среди опубликованных");
        }
        List<Request> requests = requestRepository.findAllByEvent(event);
        for (Request request : requests) {
            if (request.getEvent().getId().equals(event.getId())) {
                throw new RequestAlreadyExistException("Такая заявка на участие в этом событии уже существует");
            }
        }
    }
}

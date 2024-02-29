package ru.practicum.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.enums.RequestStatus;
import ru.practicum.exception.EventNotFoundException;
import ru.practicum.exception.RequestAlreadyExistException;
import ru.practicum.exception.RequestNotFoundException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.model.*;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    /* создаем заявку на участие в событии */
    @Override
    public Request create(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие не найдено"));
        return requestRepository.save(userId, eventId);
    }

    /* отменяем свое участие в событии */
    @Override
    public Request cansel(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Запрос на участие не найден"));
        request.setRequestStatus(RequestStatus.CANCELED);
        return requestRepository.save(request);

    }

    /* смотрим, на участие в каких событиях заявились */
    @Override
    public List<Request> getRequestsByUserId(Long userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return requests;
    }

    @Override
    public List<Request> getRequestsByEventId(Long eventId) {
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return requests;
    }

    @Override
    @Transactional
    public RequestsByStatus updateRequestsStatusByEvent(RequestStatusUpdate statusUpdate, Event event) {
        int space = getAvailableSpace(event);
        RequestStatus requestStatus = RequestStatus.valueOf(statusUpdate.getRequestStatus().toString());
        List<Long> requestIds = statusUpdate.getRequestIds();
        List<Request> requests = requestRepository.findAllById(requestIds);
        requests = requestRepository.saveAll(modifyStatusRequests(requests, space, requestStatus));
        return toRequestsByStatus(requests);
    }

    private Integer getAvailableSpace(Event event) {
        Integer limit = event.getParticipantLimit();
        List<Request> confirmedRequest = requestRepository.getRequestByStatusIs(RequestStatus.CONFIRMED, event);
        int availableSpace = limit - confirmedRequest.size();
        if (availableSpace == 0) {
            throw new RequestAlreadyExistException("Исчерпан лимит на количество участников");
        }
        return availableSpace;
    }

    private List<Request> modifyStatusRequests(List<Request> requests, int space, RequestStatus requestStatus) {
        for (Request request : requests) {
            if (!Objects.equals(request.getRequestStatus(), RequestStatus.PENDING)) {
                throw new RequestAlreadyExistException("Некорректный лимит участников");
            }
            if (space > 0) {
                request.setRequestStatus(requestStatus);
                if (Objects.equals(requestStatus, RequestStatus.CONFIRMED)) {
                    space = space - 1;
                }
            } else {
                request.setRequestStatus(RequestStatus.REJECTED);
            }
        }
        return requests;
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
}

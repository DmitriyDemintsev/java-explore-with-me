package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.enums.RequestStatus;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    int countByEventAndRequestStatus(Event event, RequestStatus statusRequest);

    List<Request> findAllByRequester(User requester);

    List<Request> findAllByEvent(Event event);

    @Query("select r from Request r where r.requestStatus = :status and r.event = :event")
    List<Request> getRequestByStatusIs(RequestStatus status, Event event);

    Request findByRequesterAndEvent(User requester, Event event);
}

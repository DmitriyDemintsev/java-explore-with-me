package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.enums.RequestStatus;
import ru.practicum.model.Event;
import ru.practicum.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Request save(Long userId, Long eventId);

    int countByEventAndStatusRequest(Event event, RequestStatus statusRequest);

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEventId(Long eventId);

    @Query("select r from Request r where r.status = :status and r.event = :event")
    List<Request> getRequestByStatusIs(RequestStatus status, Event event);

}

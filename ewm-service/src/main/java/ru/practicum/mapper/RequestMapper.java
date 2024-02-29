package ru.practicum.mapper;

import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestsByStatusDto;
import ru.practicum.model.RequestsByStatus;
import ru.practicum.enums.RequestStatus;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestMapper {

    public static Request toRequest(User user, Event event) {
        LocalDateTime current = LocalDateTime.now();
        return Request.builder()
                .requestStatus(event.getRequestModeration() ? RequestStatus.PENDING : RequestStatus.CONFIRMED)
                .created(current)
                .event(event)
                .requester(user)
                .build();
    }

    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .created(request.getCreated().toString())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getRequestStatus().toString())
                .build();
    }

    public static List<RequestDto> toRequestDtoList(Iterable<Request> requests) {
        List<RequestDto> result = new ArrayList<>();
        for (Request request: requests) {
            result.add(toRequestDto(request));
        }
        return result;
    }

//    public static RequestsByStatus toRequestsByStatusDto(List<Request> requests) {
//        List<RequestDto> confirmedRequests = new ArrayList<>();
//        List<RequestDto> rejectedRequests = new ArrayList<>();
//        for (Request request : requests) {
//            if (Objects.equals(request.getRequestStatus(), RequestStatus.CONFIRMED)) {
//                confirmedRequests.add(toRequestDto(request));
//            } else {
//                rejectedRequests.add(toRequestDto(request));
//            }
//        }
//        return new RequestsByStatus(confirmedRequests, rejectedRequests);
//
//    }

    public static RequestsByStatusDto toRequestsByStatusDto(List<Request> requests) {
        List<RequestDto> confirmedRequests = new ArrayList<>();
        List<RequestDto> rejectedRequests = new ArrayList<>();
        for (Request request : requests) {
            if (Objects.equals(request.getRequestStatus(), RequestStatus.CONFIRMED)) {
                confirmedRequests.add(toRequestDto(request));
            } else {
                rejectedRequests.add(toRequestDto(request));
            }
        }
        return new RequestsByStatusDto(confirmedRequests, rejectedRequests);

    }
}

package ru.practicum.mapper;

import ru.practicum.dto.event.*;
import ru.practicum.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventMapper {

    public static Event toCreatedEvent(NewEventDto newEventDto, Long id) {
        Event event = new Event(
                id,
                newEventDto.getAnnotation(),
                null,
                null,
                newEventDto.getDescription(),
                newEventDto.getEventDate(),
                null,
                LocationMapper.toLocation(newEventDto.getLocation()),
                newEventDto.isPaid(),
                newEventDto.getParticipantLimit(),
                null,
                newEventDto.getRequestModeration() != null ? newEventDto.getRequestModeration() : true,
                null, //
                newEventDto.getTitle(),
                null,
                null);
        return event;
    }

    public static Event byUpdateEventForAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        Event event = new Event(
                eventId,
                updateEventAdminRequest.getAnnotation(),
                null,
                null,
                updateEventAdminRequest.getDescription(),
                updateEventAdminRequest.getEventDate(),
                null,
                updateEventAdminRequest.getLocation() != null
                        ? LocationMapper.toLocation(updateEventAdminRequest.getLocation())
                        : null,
                updateEventAdminRequest.getPaid(),
                updateEventAdminRequest.getParticipantLimit(),
                null,
                updateEventAdminRequest.getRequestModeration(),
                null,
                updateEventAdminRequest.getTitle(),
                null,
                null
        );
        return event;
    }

    public static Event byUpdateEventForUser(UpdateEventUserRequest updateEventUserRequest, Long eventId) {
        Event event = new Event(
                eventId,
                updateEventUserRequest.getAnnotation(),
                null,
                null,
                updateEventUserRequest.getDescription(),
                updateEventUserRequest.getEventDate(),
                null,
                updateEventUserRequest.getLocation() != null
                        ? LocationMapper.toLocation(updateEventUserRequest.getLocation())
                        : null,
                updateEventUserRequest.getPaid(),
                updateEventUserRequest.getParticipantLimit(),
                null,
                updateEventUserRequest.getRequestModeration(),
                null,
                updateEventUserRequest.getTitle(),
                null,
                null
        );
        return event;
    }

    public static EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                LocationMapper.toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getEventState(),
                event.getTitle(),
                event.getViews());
//                event.getComments()); // для фичи
        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getConfirmedRequests(),
                event.getViews());
//                event.getComments());  // для фичи
        return eventShortDto;
    }

    public static List<EventShortDto> toShortDtos(List<Event> events) {
        List<EventShortDto> shortsDtos = new ArrayList<>();
        for (Event event : events) {
            shortsDtos.add(toEventShortDto(event));
        }
        return shortsDtos;
    }

    public static List<EventFullDto> toEventFullDtoList(Iterable<Event> events) {
        List<EventFullDto> result = new ArrayList<>();
        for (Event event : events) {
            result.add(toEventFullDto(event));
        }
        return result;
    }
}

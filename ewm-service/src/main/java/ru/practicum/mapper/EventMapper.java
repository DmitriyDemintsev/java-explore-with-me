package ru.practicum.mapper;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
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
                newEventDto.isRequestModeration(),
                null,
                newEventDto.getTitle(),
                null,
                null);
        return event;
    }

    public static Event byUpdateEvent(EventFullDto eventFullDto, Long id) {
        Event event = new Event(
                id,
                eventFullDto.getAnnotation(),
                CategoryMapper.toCategory(eventFullDto.getCategory(), id),
                eventFullDto.getCreatedOn(),
                eventFullDto.getDescription(),
                eventFullDto.getEventDate(),
                null,
                LocationMapper.toLocation(eventFullDto.getLocation()),
                eventFullDto.getPaid(),
                eventFullDto.getParticipantLimit(),
                eventFullDto.getPublishedOn(),
                eventFullDto.getRequestModeration(),
                eventFullDto.getEventState(),
                eventFullDto.getTitle(),
                eventFullDto.getConfirmedRequests(),
                eventFullDto.getViews());
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
//                event.getComments());
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
//                event.getComments());
        return eventShortDto;
    }

    public static NewEventDto toNewEventDto(Event event) {
        NewEventDto newEventDto = new NewEventDto(
                event.getAnnotation(),
                event.getCategory().getId(),
                event.getDescription(),
                event.getEventDate(),
                LocationMapper.toLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getRequestModeration(),
                event.getTitle());
        return newEventDto;
    }

    public static List<EventFullDto> toEventFullDtoList(Iterable<Event> events) {
        List<EventFullDto> result = new ArrayList<>();
        for (Event event: events) {
            result.add(toEventFullDto(event));
        }
        return result;
    }
}

package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestsByStatusDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.RequestStatusUpdate;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import java.util.List;

@Component
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {
    private final EventService eventService;

    /* пользователь создает событие */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        return EventMapper.toEventFullDto(eventService.create(userId,
                EventMapper.toCreatedEvent(newEventDto, null), newEventDto.getCategoryId()));
    }

    /* пользователь редактирует событие */
    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable("userId") Long userId,
                                    @PathVariable("eventId") Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {

        return EventMapper.toEventFullDto(eventService.updateEvent(userId, updateEventUserRequest.getCategory(),
                EventMapper.byUpdateEventForUser(updateEventUserRequest, eventId),
                updateEventUserRequest.getStateAction()));
    }

    /* меняем статус заявки на участие в событии */
    @PatchMapping("/{eventId}/requests")
    public RequestsByStatusDto updateRequestStatusEvent(@PathVariable("userId") Long userId,
                                                        @PathVariable("eventId") Long eventId,
                                                        @Valid @RequestBody RequestStatusUpdate request) {
        return RequestMapper.toRequestsByStatusDtoForEvent(eventService.updateRequestStatusEvent(userId,
                eventId, request));
    }

    /* получаем список событий, добавленных текущим пользователем */
    @GetMapping
    public List<EventFullDto> getListsEvents(@PathVariable long userId,
                                             @RequestParam(required = false, defaultValue = "0") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        return EventMapper.toEventFullDtoList(eventService.getListsEvents(userId, null,
                null, null, null, null, null, null,
                null, null, from, size));
    }

    /* полная информация о событии, добавленном текущим пользователем */
    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable(required = false) Long userId,
                                     @PathVariable(required = false) Long eventId) {
        return EventMapper.toEventFullDto(eventService.getEventById(userId, eventId, null));
    }

    /* список запросов на участие в событии текущего пользователя */
    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsByEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return RequestMapper.toRequestDtoList(eventService.getRequestsByEventId(userId, eventId));
    }
}

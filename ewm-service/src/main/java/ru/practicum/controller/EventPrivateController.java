package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestsByStatusDto;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Request;
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
    public EventFullDto create(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        return EventMapper.toEventFullDto(eventService.create(userId,
                EventMapper.toCreatedEvent(newEventDto, null)));
    }

    /* пользователь редактирует событие */
    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable("userId") Long userId,
                                    @PathVariable("eventId") Long eventId,
                                    @Valid @RequestBody EventFullDto eventFullDto) {
        return EventMapper.toEventFullDto(eventService.updateEvent(userId, eventId,
                EventMapper.byUpdateEvent(eventFullDto, null)));
    }

    /* меняем статус заявки на участие в событии */
    @PatchMapping("/{eventId}/requests")
    public RequestsByStatusDto updateRequestStatusEvent(@PathVariable("userId") Long userId,
                                                        @PathVariable("eventId") Long eventId,
                                                        @Valid @RequestBody RequestStatusUpdate requestStatusUpdateDto) {
        return RequestMapper.toRequestsByStatusDto((List<Request>) eventService.updateRequestStatusEvent(userId,
                eventId, requestStatusUpdateDto));
    }

    /* получаем список событий, добавленных текущим пользователем */
    @GetMapping
    public List<EventFullDto> getListsEvents(@RequestParam(required = false) Long userId,
                                             @RequestParam(required = false, defaultValue = "0") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        return EventMapper.toEventFullDtoList(eventService.getListsEvents(userId, null, null,
                null, null, null, null, null, null,
                null, null, from, size));
    }

    /* полная информация о событии, добавленном текущим пользователем */
    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@RequestParam(required = false) Long userId,
                                     @PathVariable(required = false) Long eventId) {
        return EventMapper.toEventFullDto(eventService.getEventById(userId, eventId));
    }

    /* список запросов на участие в событии текущего пользователя */
    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsByEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return RequestMapper.toRequestDtoList(eventService.getRequestsByEventId(userId, eventId));
    }
}

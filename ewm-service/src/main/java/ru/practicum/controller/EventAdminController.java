package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.mapper.EventMapper;
import ru.practicum.service.event.EventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {
    private final EventService eventService;

    /* модерация события и публикация/отклонение */
    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable("eventId") Long eventId,
                                    @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {

        return EventMapper.toEventFullDto(eventService.updateEvent(null, updateEventAdminRequest.getCategory(),
                EventMapper.byUpdateEventForAdmin(updateEventAdminRequest, eventId),
                updateEventAdminRequest.getStateAction()));
    }

    /* поиск события по фильтрам */
    @GetMapping
    public List<EventFullDto> getListsEvents(@RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<String> states,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) LocalDateTime rangeStart,
                                             @RequestParam(required = false) LocalDateTime rangeEnd,
                                             @RequestParam(required = false, defaultValue = "0") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        return EventMapper.toEventFullDtoList(eventService.getListsEvents(null, users, states,
                null, categories, null, rangeStart, rangeEnd, null, null, from, size));
    }
}

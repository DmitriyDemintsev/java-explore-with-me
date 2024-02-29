package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.service.event.EventService;

import java.util.List;

@Component
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventPublicController {
    private final EventService eventService;

    /* поиск событий по фильтрам */
    @GetMapping
    public List<EventFullDto> getListsEvents(@RequestParam(required = false) String text,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) Boolean paid,
                                             @RequestParam(required = false) String rangeStart,
                                             @RequestParam(required = false) String rangeEnd,
                                             @RequestParam(required = false) Boolean onlyAvailable,
                                             @RequestParam(required = false) String sort,
                                             @RequestParam(required = false, defaultValue = "0") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        return EventMapper.toEventFullDtoList(eventService.getListsEvents(null, null, null,
                null, text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size));
    }

    /* подробная информация о событии */
    @GetMapping("/{id}")
    public EventFullDto getEventById(@RequestParam(required = false) Long id) {
        return EventMapper.toEventFullDto(eventService.getEventById(id, null));
    }
}

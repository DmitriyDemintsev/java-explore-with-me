package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
import ru.practicum.dto.StatsDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.mapper.EventMapper;
import ru.practicum.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventPublicController {
    private final EventService eventService;
    private final StatsClient statsClient;

    /**
     * поиск событий по фильтрам
     */
    @GetMapping
    public List<EventShortDto> getListsEvents(@RequestParam(required = false) String text,
                                              @RequestParam(required = false) List<Long> categories,
                                              @RequestParam(required = false) Boolean paid,
                                              @RequestParam(required = false) LocalDateTime rangeStart,
                                              @RequestParam(required = false) LocalDateTime rangeEnd,
                                              @RequestParam(required = false) Boolean onlyAvailable,
                                              @RequestParam(required = false) String sort,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                              @Positive @RequestParam(defaultValue = "10") int size,
                                              HttpServletRequest request) {
        registerView(null, request.getRemoteAddr());
        return EventMapper.toShortDtos(eventService.getListsEvents(null, null,
                null, text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size));
    }

    /**
     * подробная информация о событии
     */
    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable("id") Long eventId, HttpServletRequest request) {
        registerView(eventId, request.getRemoteAddr());
        return EventMapper.toEventFullDto(eventService.getEventById(null, eventId, request.getRemoteAddr()));
    }

    private void registerView(Long eventId, String ip) {
        StatsDto dto = new StatsDto(
                null,
                "ewm-service",
                "/events" + (eventId != null ? "/" + eventId : ""),
                ip,
                LocalDateTime.now()
        );
        statsClient.createStats(dto);
    }
}

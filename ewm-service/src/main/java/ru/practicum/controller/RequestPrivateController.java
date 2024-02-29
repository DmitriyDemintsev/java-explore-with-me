package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestsByStatusDto;
import ru.practicum.mapper.RequestMapper;

import ru.practicum.service.request.RequestService;

@Component
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class RequestPrivateController {
    private final RequestService requestService;

    /* создаем заявку на участие в событии */
    @PostMapping
    public RequestDto create(@PathVariable Long userId,
                                   @PathVariable Long eventId) {
        return RequestMapper.toRequestDto(requestService.create(userId, eventId));
    }

    /* отменяем свое участие в событии */
    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return RequestMapper.toRequestDto(requestService.cansel(userId, requestId));
    }

    /* смотрим, на участие в каких событиях заявились */
    @GetMapping
    public RequestsByStatusDto getRequest (@PathVariable Long userId) {
        return RequestMapper.toRequestsByStatusDto(requestService.getRequestsByUserId(userId));
    }
}

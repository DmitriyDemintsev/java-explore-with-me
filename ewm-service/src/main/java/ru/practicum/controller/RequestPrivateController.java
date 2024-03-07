package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.service.request.RequestService;

import java.util.List;

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
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto create(@PathVariable Long userId,
                             @RequestParam Long eventId) {
        return RequestMapper.toRequestDto(requestService.create(userId, eventId));
    }

    /* отменяем свое участие в событии */
    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return RequestMapper.toRequestDto(requestService.cansel(userId, requestId));
    }

    /* смотрим, на участие в каких событиях заявились */
    @GetMapping
    public List<RequestDto> getRequest(@PathVariable Long userId) {
        return RequestMapper.toRequestDtoList(requestService.getRequestsByUserId(userId));
    }
}

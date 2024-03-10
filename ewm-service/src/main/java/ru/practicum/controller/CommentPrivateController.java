package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentCreateDto;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentUpdateDto;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;

@Component
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class CommentPrivateController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable long userId,
                                    @Valid @RequestBody CommentCreateDto commentCreateDto) {

        return CommentMapper.toCommentDto(commentService.create(userId, commentCreateDto.getEventId(),
                CommentMapper.toCreateComment(commentCreateDto)));
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable("userId") long userId,
                                    @PathVariable("commentId") long commentId,
                                    @Valid @RequestBody CommentUpdateDto commentUpdateDto) {
        return CommentMapper.toCommentDto(commentService.update(userId,
                CommentMapper.toUpdateComment(commentUpdateDto, commentId)));
    }
}

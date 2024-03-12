package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.comment.CommentService;

@Component
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events/{commentId}")
public class CommentAdminController {
    private final CommentService commentService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }
}

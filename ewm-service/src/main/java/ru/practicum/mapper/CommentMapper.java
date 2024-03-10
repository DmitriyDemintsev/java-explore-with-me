package ru.practicum.mapper;

import ru.practicum.dto.comment.CommentCreateDto;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentUpdateDto;
import ru.practicum.model.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static Comment toCreateComment(CommentCreateDto commentCreateDto) {
        return new Comment(
                null,
                commentCreateDto.getText(),
                null,
                null,
                LocalDateTime.now()
        );
    }

    public static Comment toUpdateComment(CommentUpdateDto commentUpdateDto, long commentId) {
        return new Comment(
                commentId,
                commentUpdateDto.getText(),
                null,
                null,
                null
        );
    }


    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                EventMapper.toEventShortDto(comment.getEvent()),
                UserMapper.toUserShortDto(comment.getAuthor()),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static List<CommentDto> toCommentDtoList(Iterable<Comment> comments) {
        List<CommentDto> result = new ArrayList<>();
        for (Comment comment : comments) {
            result.add(toCommentDto(comment));
        }
        return result;
    }
}
package ru.practicum.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CommentCreateDto implements Serializable {
    private long eventId;
    private String text; //содержимое комментария
}

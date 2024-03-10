package ru.practicum.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentUpdateDto {
    private String text; //содержимое комментария

}

package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.user.UserShortDto;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto implements Serializable {
    private Long id; // идентификатор комментария
    private String text; //содержимое комментария
    private EventShortDto event; //вещь, к которой относится комментарий
    private UserShortDto author; //автор комментария - тот, кто пользовался вещью
    private String authorName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created; //дата создания комментария
}

package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StatsDto {
    private Integer id;
    @NotBlank(message = "Имя приложения не может быть пустым")
    private String app; // сервис, для которого записывается информация (ZB: наш сервис)
    @NotBlank(message = "Строка для обращения в DB не может быть пустой")
    private String uri; // URI, для которого сделал запрос
    @NotBlank(message = "IP-адрес запроса в DB не может быть пустым")
    private String ip; // кто сделал запрос (IP-адрес пользователя)
    @NotNull(message = "Отсутствуют данные даты/времени запроса в DB")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp; // когда сделан запрос (день/время)
}

package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.enums.StateAction;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UpdateEventUserRequest implements Serializable {

    @Size(min = 20, max = 2000, message = "Знаков в аннотации должно быть от 20 до 2000")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Знаков в аннотации должно быть от 20 до 7000")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Size(min = 3, max = 120, message = "Знаков в заголовке должно быть от 3 до 120")
    private String title;
}

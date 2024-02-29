package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.dto.location.LocationDto;

import javax.persistence.Embedded;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto implements Serializable {

    @NotBlank
    @Size(min = 20, max = 2000, message = "Знаков в аннотации должно быть от 20 до 2000")
    private String annotation;
    @JsonProperty("category")
    @NotNull
    private Long categoryId;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Знаков в описании должно быть от 20 до 7000")
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @Embedded
    private LocationDto location;
    private boolean paid;
    @PositiveOrZero
    private int participantLimit;
    private boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120, message = "Знаков в заголовке должно быть от 3 до 120")
    private String title;
}

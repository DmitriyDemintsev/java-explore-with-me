package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.enums.EventState;

import javax.persistence.Embedded;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventFullDto implements Serializable {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    @Embedded
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    @JsonProperty("state")
    private EventState eventState;
    private String title;
    private Integer views;
}

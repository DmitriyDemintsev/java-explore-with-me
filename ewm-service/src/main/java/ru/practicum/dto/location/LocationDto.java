package ru.practicum.dto.location;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class LocationDto implements Serializable {
    private Float lat;
    private Float lon;
}

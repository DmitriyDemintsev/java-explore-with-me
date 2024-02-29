package ru.practicum.model;

import lombok.*;

import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@Embeddable
public class Location {
    private Float lat;
    private Float lon;
}

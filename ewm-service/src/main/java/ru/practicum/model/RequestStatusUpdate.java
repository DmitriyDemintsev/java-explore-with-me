package ru.practicum.model;

import lombok.*;
import ru.practicum.enums.RequestStatus;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RequestStatusUpdate implements Serializable {
    private List<Long> requestIds;
    private RequestStatus requestStatus;
}
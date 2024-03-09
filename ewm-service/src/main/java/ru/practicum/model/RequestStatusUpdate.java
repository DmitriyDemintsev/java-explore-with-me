package ru.practicum.model;

import lombok.*;
import ru.practicum.enums.RequestStatus;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RequestStatusUpdate implements Serializable {
    @NotNull
    private List<Long> requestIds;
    @NotNull
    private RequestStatus status;
}
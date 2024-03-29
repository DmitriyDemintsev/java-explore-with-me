package ru.practicum.dto.request;

import lombok.*;
import ru.practicum.enums.StatusUpdate;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RequestStatusUpdateDto {
    @NotNull
    private List<Long> requestIds;
    @NotNull
    private StatusUpdate status;
}


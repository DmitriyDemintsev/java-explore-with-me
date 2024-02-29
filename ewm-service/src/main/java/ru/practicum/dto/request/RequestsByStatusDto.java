package ru.practicum.dto.request;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RequestsByStatusDto implements Serializable {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}

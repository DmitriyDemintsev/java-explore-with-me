package ru.practicum.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RequestsByStatus {
    private List<Request> confirmedRequests;
    private List<Request> rejectedRequests;
}

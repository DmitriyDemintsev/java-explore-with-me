package ru.practicum.dto.request;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RequestDto implements Serializable {
    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private String status;
}

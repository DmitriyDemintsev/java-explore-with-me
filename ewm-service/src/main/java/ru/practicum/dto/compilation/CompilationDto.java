package ru.practicum.dto.compilation;

import lombok.*;
import ru.practicum.dto.event.EventShortDto;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CompilationDto implements Serializable {
    private Long id;
    private List<EventShortDto> events;
    private boolean pinned;
    private String title;
}

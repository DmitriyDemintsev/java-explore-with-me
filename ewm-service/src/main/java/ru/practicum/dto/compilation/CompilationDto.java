package ru.practicum.dto.compilation;

import lombok.*;
import ru.practicum.dto.event.EventShortDto;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    private Boolean pinned;
    @NotBlank
    @Size(min = 20, max = 50, message = "Количество символов в заголовке - от 20 до 50")
    private String title;

}

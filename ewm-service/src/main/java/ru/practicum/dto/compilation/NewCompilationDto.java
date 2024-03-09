package ru.practicum.dto.compilation;

import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NewCompilationDto implements Serializable {
    private Set<Long> events;
    private Boolean pinned;
    @Size(min = 1, max = 50, message = "Количество символов в заголовке - не более 50")
    private String title;

    public Boolean getPinned() {
        return !Objects.isNull(pinned) && pinned;
    }
}

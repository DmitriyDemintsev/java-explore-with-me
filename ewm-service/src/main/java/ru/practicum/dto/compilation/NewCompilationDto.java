package ru.practicum.dto.compilation;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NewCompilationDto implements Serializable {
    private List<Long> events;
    private boolean pinned;
    @NotBlank
    @Size(max = 50, message = "Количество символов в заголовке - до 50")
    private String title;

    public Boolean getPinned() {
        return !Objects.isNull(pinned) && pinned;
    }
}

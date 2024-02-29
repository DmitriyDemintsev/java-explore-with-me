package ru.practicum.dto.compilation;

import lombok.*;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest implements Serializable {
    private List<Long> events;
    private Boolean pinned;
    @Size(max = 50, message = "Количество символов в заголовке - до 50")
    private String title;
}

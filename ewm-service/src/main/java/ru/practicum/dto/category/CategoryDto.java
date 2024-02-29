package ru.practicum.dto.category;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto implements Serializable {
    private Long id;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50, message = "Количество символов в названии категории - от 1 до 50")
    private String name;
}

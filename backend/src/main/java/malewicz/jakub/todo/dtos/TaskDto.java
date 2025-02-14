package malewicz.jakub.todo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskDto(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotNull
        LocalDate date
) {
}

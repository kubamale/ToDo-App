package malewicz.jakub.todo.dtos;

import java.time.LocalDate;
import java.util.UUID;

public record TaskDetailsDto(
        UUID id,
        String title,
        String description,
        LocalDate date
) {
}

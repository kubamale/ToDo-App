package malewicz.jakub.todo.dtos;

import malewicz.jakub.todo.entities.Status;

import java.time.LocalDate;
import java.util.UUID;

public record TaskDetailsDto(
        UUID id,
        String title,
        String description,
        LocalDate date,
        Status status
) {
}

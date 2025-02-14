package malewicz.jakub.todo.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import malewicz.jakub.todo.dtos.TaskDetailsDto;
import malewicz.jakub.todo.dtos.TaskDto;
import malewicz.jakub.todo.mappers.TaskMapper;
import malewicz.jakub.todo.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskDetailsDto createTask(final TaskDto taskDto) {
        var task = taskMapper.toTaskEntity(taskDto);
        return taskMapper.toTaskDetailsDto(taskRepository.save(task));
    }

    public List<LocalDate> getTaskDates(LocalDate startDate, LocalDate endDate) {
        return taskRepository.findTaskDatesBetween(startDate, endDate);
    }

    public List<TaskDetailsDto> getTasksByDate(LocalDate date) {
        return taskRepository.findByDate(date).stream().map(taskMapper::toTaskDetailsDto).toList();
    }

    public void deleteTask(UUID id) {
        taskRepository.findById(id).ifPresentOrElse(taskRepository::delete, () -> {
            throw new EntityNotFoundException("Task not found with id " + id);
        });
    }
}

package malewicz.jakub.todo.services;

import lombok.RequiredArgsConstructor;
import malewicz.jakub.todo.dtos.TaskDetailsDto;
import malewicz.jakub.todo.dtos.TaskDto;
import malewicz.jakub.todo.mappers.TaskMapper;
import malewicz.jakub.todo.repositories.TaskRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskDetailsDto createTask(final TaskDto taskDto) {
        var task = taskMapper.toTaskEntity(taskDto);
        return taskMapper.toTaskDetailsDto(taskRepository.save(task));
    }
}

package malewicz.jakub.todo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import malewicz.jakub.todo.dtos.TaskDetailsDto;
import malewicz.jakub.todo.dtos.TaskDto;
import malewicz.jakub.todo.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TaskDetailsDto createTask(@Valid @RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @GetMapping("/dates")
    public List<LocalDate> getTaskDates(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return taskService.getTaskDates(startDate, endDate);
    }

    @GetMapping
    public List<TaskDetailsDto> getTasks(@RequestParam LocalDate date) {
        return taskService.getTasksByDate(date);
    }
}

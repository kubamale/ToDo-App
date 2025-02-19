package malewicz.jakub.todo.services;

import jakarta.persistence.EntityNotFoundException;
import malewicz.jakub.todo.dtos.TaskDetailsDto;
import malewicz.jakub.todo.dtos.TaskDto;
import malewicz.jakub.todo.entities.TaskEntity;
import malewicz.jakub.todo.mappers.TaskMapper;
import malewicz.jakub.todo.repositories.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskMapper taskMapper;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskService taskService;

    @Test
    void testCreateTask_shouldReturnTaskDetails() {
        var taskDto = new TaskDto("Clean", "Clean my room.", LocalDate.now());
        var mappedTaskEntity = TaskEntity.builder().title(taskDto.title()).date(taskDto.date()).description(taskDto.description()).build();
        var taskDetails = new TaskDetailsDto(UUID.randomUUID(), taskDto.title(), taskDto.description(), taskDto.date());
        when(taskMapper.toTaskEntity(taskDto)).thenReturn(mappedTaskEntity);
        when(taskRepository.save(mappedTaskEntity)).thenReturn(mappedTaskEntity);
        when(taskMapper.toTaskDetailsDto(mappedTaskEntity)).thenReturn(taskDetails);
        var task = taskService.createTask(taskDto);
        assertThat(task).isEqualTo(taskDetails);
    }

    @Test
    void getTaskDates_shouldReturnTaskDates() {
        var startDate = LocalDate.now().minusDays(1);
        var endDate = LocalDate.now().plusDays(1);
        when(taskRepository.findTaskDatesBetween(startDate, endDate)).thenReturn(List.of(LocalDate.now().minusDays(1), LocalDate.now().minusDays(2)));
        var dates = taskService.getTaskDates(startDate, endDate);
        assertThat(dates.size()).isEqualTo(2);
    }

    @Test
    void getTasksByDate_shouldReturnTasksForSpecifiedDate() {
        var date = LocalDate.now();
        var task = TaskEntity.builder().title("Clean").date(date).id(UUID.randomUUID()).description("Clean my room.").build();
        var taskDetails = new TaskDetailsDto(task.getId(), task.getTitle(), task.getDescription(), task.getDate());
        when(taskRepository.findByDate(date)).thenReturn(List.of(task));
        when(taskMapper.toTaskDetailsDto(task)).thenReturn(taskDetails);
        var tasks = taskService.getTasksByDate(date);
        assertThat(tasks.size()).isEqualTo(1);
        assertThat(tasks.getFirst()).isEqualTo(taskDetails);
    }

    @Test
    void deleteTask_shouldThrowEntityNotFoundException() {
        var taskId = UUID.randomUUID();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> taskService.deleteTask(taskId));
    }

    @Test
    void deleteTask_shouldThrowDeleteTask() {
        var taskId = UUID.randomUUID();
        var task = TaskEntity.builder().title("Clean").date(LocalDate.now()).id(UUID.randomUUID()).description("Clean my room.").build();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        taskService.deleteTask(taskId);
        verify(taskRepository).delete(task);
    }

    @Test
    void updateTask_shouldThrowEntityNotFoundException() {
        var taskId = UUID.randomUUID();
        var taskDto = new TaskDto("Updated", "Updated description.", LocalDate.now());
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> taskService.updateTask(taskId, taskDto));
    }

    @Test
    void updateTask_shouldReturnUpdatedTaskDetails() {
        var taskId = UUID.randomUUID();
        var taskDto = new TaskDto("Updated", "Updated description.", LocalDate.now());
        var task = TaskEntity.builder().title("Clean").date(LocalDate.now()).id(UUID.randomUUID()).description("Clean my room.").build();
        var updatedTask = TaskEntity.builder().title(taskDto.title()).date(taskDto.date()).id(taskId).description(taskDto.description()).build();
        var taskDetails = new TaskDetailsDto(updatedTask.getId(), updatedTask.getTitle(), updatedTask.getDescription(), updatedTask.getDate());
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskDetailsDto(task)).thenReturn(taskDetails);
        var result = taskService.updateTask(taskId, taskDto);
        assertThat(result.title()).isEqualTo(taskDto.title());
        assertThat(result.description()).isEqualTo(taskDto.description());
        assertThat(result.date()).isEqualTo(taskDto.date());
    }
}
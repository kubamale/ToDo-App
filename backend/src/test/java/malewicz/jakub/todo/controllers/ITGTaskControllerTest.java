package malewicz.jakub.todo.controllers;

import malewicz.jakub.todo.TestcontainersConfiguration;
import malewicz.jakub.todo.dtos.TaskDetailsDto;
import malewicz.jakub.todo.dtos.TaskDto;
import malewicz.jakub.todo.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITGTaskControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testCreateTaskShouldReturnCreatedTask() {
        var requestBody = new TaskDto("Clean", "Clean my room.", LocalDate.now());
        var response = restTemplate.exchange("/api/v1/tasks", HttpMethod.POST, new HttpEntity<>(requestBody), TaskDetailsDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testCreateTaskShouldReturnBadRequestErrorWhenPassedInvalidTitle() {
        var requestBody = new TaskDto("", "Clean my room.", LocalDate.now());
        var response = restTemplate.exchange("/api/v1/tasks", HttpMethod.POST, new HttpEntity<>(requestBody), TaskDetailsDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testCreateTaskShouldReturnBadRequestErrorWhenPassedInvalidDescription() {
        var requestBody = new TaskDto("Clean", "", LocalDate.now());
        var response = restTemplate.exchange("/api/v1/tasks", HttpMethod.POST, new HttpEntity<>(requestBody), TaskDetailsDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testCreateTaskShouldReturnBadRequestErrorWhenPassedInvalidDate() {
        var requestBody = new TaskDto("Clean", "Clean my room.", null);
        var response = restTemplate.exchange("/api/v1/tasks", HttpMethod.POST, new HttpEntity<>(requestBody), TaskDetailsDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testGetTaskDatesShouldReturnAllDistinctDates() {
        var response = restTemplate.getForObject(
                "/api/v1/tasks/dates?startDate={startDate}&endDate={endDate}",
                LocalDate[].class,
                LocalDate.now().minusYears(1),
                LocalDate.now().plusYears(1)
        );
        assertThat(response).isNotNull();
        assertThat(Integer.valueOf(response.length)).isEqualTo(1);
    }

    @Test
    void testGetTasksByDateShouldReturnAllTasksInGivenDate() {
        var tasksAmount = taskRepository.count();
        var response = restTemplate.getForObject(
                "/api/v1/tasks?date={date}",
                TaskDetailsDto[].class,
                LocalDate.now()
        );
        assertThat(response).isNotNull();
        assertThat(Integer.valueOf(response.length)).isEqualTo(tasksAmount);
    }

    @Test
    void testDeleteTaskShouldReturnNoContent() {
        var tasks = taskRepository.findAll();
        var response = restTemplate.exchange(
                "/api/v1/tasks/" + tasks.getFirst().getId().toString(),
                HttpMethod.DELETE,
                null,
                ResponseEntity.class,
                LocalDate.now()
        );
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(taskRepository.count()).isEqualTo(tasks.size() - 1);
    }
}


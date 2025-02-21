package malewicz.jakub.todo.controllers;

import malewicz.jakub.todo.TestcontainersConfiguration;
import malewicz.jakub.todo.dtos.FilterDto;
import malewicz.jakub.todo.dtos.TaskDetailsDto;
import malewicz.jakub.todo.dtos.TaskDto;
import malewicz.jakub.todo.entities.TaskEntity;
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
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITGTaskControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        var tasks = List.of(
                TaskEntity.builder()
                        .title("Clean")
                        .description("Clean my room.")
                        .date(LocalDate.now())
                        .build(),
                TaskEntity.builder()
                        .title("Exercise")
                        .description("Do stretching exercises.")
                        .date(LocalDate.now())
                        .build()
        );
        taskRepository.saveAll(tasks);
    }

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
        var response = restTemplate.exchange("/api/v1/tasks", HttpMethod.POST, new HttpEntity<>(requestBody), Object.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testCreateTaskShouldReturnBadRequestErrorWhenPassedInvalidDescription() {
        var requestBody = new TaskDto("Clean", "", LocalDate.now());
        var response = restTemplate.exchange("/api/v1/tasks", HttpMethod.POST, new HttpEntity<>(requestBody), Object.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testCreateTaskShouldReturnBadRequestErrorWhenPassedInvalidDate() {
        var requestBody = new TaskDto("Clean", "Clean my room.", null);
        var response = restTemplate.exchange("/api/v1/tasks", HttpMethod.POST, new HttpEntity<>(requestBody), Object.class);
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

    @Test
    void testUpdateTaskShouldReturnUpdatedTask() {
        var requestBody = new TaskDto("Updated", "Updated description", LocalDate.now());
        var task = taskRepository.findAll().getFirst();
        assert task != null;
        var response = restTemplate.exchange("/api/v1/tasks/" + task.getId(), HttpMethod.PUT, new HttpEntity<>(requestBody), TaskDetailsDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        var body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.title()).isEqualTo(requestBody.title());
        assertThat(body.description()).isEqualTo(requestBody.description());
        assertThat(body.date()).isEqualTo(requestBody.date());
    }

    @Test
    void testMarkTaskAsCompletedShouldReturnNoContent() {
        var task = taskRepository.findAll().getFirst();
        assert task != null;
        var response = restTemplate.exchange("/api/v1/tasks/" + task.getId(), HttpMethod.PATCH, null, Object.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void testFilterTasksShouldReturnFilteredTasks() {
        var filters = List.of(new FilterDto("title", "Clean"), new FilterDto("date", LocalDate.now()));
        var response = restTemplate.exchange("/api/v1/tasks/filter", HttpMethod.POST, new HttpEntity<>(filters), TaskDetailsDto[].class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).length).isEqualTo(1);
    }

    @Test
    void testFilterTasksShouldReturnBadRequestWhenPassedInvalidFieldName() {
        var filters = List.of(new FilterDto("title", "Clean"), new FilterDto("not existing field name", LocalDate.now()));
        var response = restTemplate.exchange("/api/v1/tasks/filter", HttpMethod.POST, new HttpEntity<>(filters), Object.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void testFilterTasksShouldReturnBadRequestWhenPassedInvalidValue() {
        var filters = List.of(new FilterDto("title", "Clean"), new FilterDto("date" , "this is not valid date value"));
        var response = restTemplate.exchange("/api/v1/tasks/filter", HttpMethod.POST, new HttpEntity<>(filters), Object.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}


package malewicz.jakub.todo.controllers;

import malewicz.jakub.todo.TestcontainersConfiguration;
import malewicz.jakub.todo.dtos.TaskDetailsDto;
import malewicz.jakub.todo.dtos.TaskDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITGTaskControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Rollback
    void testCreateTaskShouldReturnCreatedTask() {
        var requestBody = new TaskDto("Clean", "Clean my room.", LocalDate.now());
        var response = restTemplate.exchange("/api/v1/tasks", HttpMethod.POST, new HttpEntity<>(requestBody), TaskDetailsDto.class);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

}
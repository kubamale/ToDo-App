package malewicz.jakub.todo;

import lombok.RequiredArgsConstructor;
import malewicz.jakub.todo.entities.TaskEntity;
import malewicz.jakub.todo.repositories.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {
    private final TaskRepository taskRepository;

    @Override
    public void run(String... args) {
        if (taskRepository.count() == 0) {
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
    }
}

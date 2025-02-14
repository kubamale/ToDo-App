package malewicz.jakub.todo.repositories;

import malewicz.jakub.todo.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
    @Query("SELECT DISTINCT t.date from tasks t where t.date >= :startDate AND t.date <= :endDate")
    List<LocalDate> findTaskDatesBetween(LocalDate startDate, LocalDate endDate);

    List<TaskEntity> findByDate(LocalDate date);
}

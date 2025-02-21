package malewicz.jakub.todo.repositories;

import malewicz.jakub.todo.entities.Status;
import malewicz.jakub.todo.entities.TaskEntity;
import malewicz.jakub.todo.entities.TaskEntity_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID>, JpaSpecificationExecutor<TaskEntity> {
    @Query("SELECT DISTINCT t.date from tasks t where t.date >= :startDate AND t.date <= :endDate")
    List<LocalDate> findTaskDatesBetween(LocalDate startDate, LocalDate endDate);

    List<TaskEntity> findByDate(LocalDate date);

    interface Specs{
        static Specification<TaskEntity> byDate(LocalDate date){
            return (root, query, builder) -> builder.equal(root.get(TaskEntity_.DATE), date);
        }

        static Specification<TaskEntity> byTitle(String title){
            return (root, query, builder) -> builder.equal(root.get(TaskEntity_.TITLE), title);
        }

        static Specification<TaskEntity> byStatus(Status status){
            return (root, query, builder) -> builder.equal(root.get(TaskEntity_.STATUS), status);
        }
    }
}

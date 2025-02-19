package malewicz.jakub.todo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@Builder
@Entity(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    @Column(nullable = false)
    private String title;
    @NotBlank
    @Column(nullable = false)
    private String description;
    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate date;
    @Builder.Default
    @Enumerated
    private Status status = Status.INCOMPLETE;
    @Version
    private int version;
}

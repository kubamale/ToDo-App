package malewicz.jakub.todo.services;

import malewicz.jakub.todo.dtos.FilterDto;
import malewicz.jakub.todo.entities.Status;
import malewicz.jakub.todo.entities.TaskEntity;
import malewicz.jakub.todo.entities.TaskEntity_;
import malewicz.jakub.todo.exceptions.BadRequestException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import static malewicz.jakub.todo.repositories.TaskRepository.Specs.*;

@Service
public class TaskFilterService implements FilterService<TaskEntity> {

    @Override
    public Specification<TaskEntity> getSpecification(List<FilterDto> filters) {
        return filters.stream().map(this::getSpecificationForFilter).reduce(Specification::and).orElse(null);
    }

    private Specification<TaskEntity> getSpecificationForFilter(FilterDto filter) {
        try {
            switch (filter.field()) {
                case TaskEntity_.DATE -> {
                    var date = LocalDate.parse((String) filter.value());
                    return byDate(date);
                }
                case TaskEntity_.STATUS -> {
                    var status = Status.valueOf((String) filter.value());
                    return byStatus(status);
                }
                case TaskEntity_.TITLE -> {
                    var title = (TaskEntity_.title.getType().getJavaType().cast(filter.value()));
                    return byTitle(title);
                }
                default -> throw new BadRequestException("Unexpected value: " + filter.field());
            }
        } catch (ClassCastException | DateTimeParseException | IllegalArgumentException e) {
            throw new BadRequestException("Unexpected value for field " + filter.field());
        }
    }
}

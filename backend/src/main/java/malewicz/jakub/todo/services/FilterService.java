package malewicz.jakub.todo.services;

import malewicz.jakub.todo.dtos.FilterDto;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface FilterService<T> {
    Specification<T> getSpecification(List<FilterDto> filters);
}

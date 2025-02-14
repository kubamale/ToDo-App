package malewicz.jakub.todo.mappers;

import malewicz.jakub.todo.dtos.TaskDetailsDto;
import malewicz.jakub.todo.dtos.TaskDto;
import malewicz.jakub.todo.entities.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    TaskEntity toTaskEntity(TaskDto task);

    TaskDetailsDto toTaskDetailsDto(TaskEntity task);
}

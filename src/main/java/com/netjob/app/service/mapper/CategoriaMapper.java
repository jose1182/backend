package com.netjob.app.service.mapper;

import com.netjob.app.domain.Categoria;
import com.netjob.app.service.dto.CategoriaDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Categoria} and its DTO {@link CategoriaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoriaMapper extends EntityMapper<CategoriaDTO, Categoria> {
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<CategoriaDTO> toDtoIdSet(Set<Categoria> categoria);
}

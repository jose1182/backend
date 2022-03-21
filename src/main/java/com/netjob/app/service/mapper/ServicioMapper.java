package com.netjob.app.service.mapper;

import com.netjob.app.domain.Servicio;
import com.netjob.app.service.dto.ServicioDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Servicio} and its DTO {@link ServicioDTO}.
 */
@Mapper(componentModel = "spring", uses = { UsuarioMapper.class, CategoriaMapper.class })
public interface ServicioMapper extends EntityMapper<ServicioDTO, Servicio> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "id")
    @Mapping(target = "categorias", source = "categorias", qualifiedByName = "idSet")
    ServicioDTO toDto(Servicio s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServicioDTO toDtoId(Servicio servicio);

    @Mapping(target = "removeCategoria", ignore = true)
    Servicio toEntity(ServicioDTO servicioDTO);
}

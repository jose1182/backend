package com.netjob.app.service.mapper;

import com.netjob.app.domain.Valoracion;
import com.netjob.app.service.dto.ValoracionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Valoracion} and its DTO {@link ValoracionDTO}.
 */
@Mapper(componentModel = "spring", uses = { UsuarioMapper.class, ServicioMapper.class })
public interface ValoracionMapper extends EntityMapper<ValoracionDTO, Valoracion> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "id")
    @Mapping(target = "servicio", source = "servicio", qualifiedByName = "id")
    ValoracionDTO toDto(Valoracion s);
}

package com.netjob.app.service.mapper;

import com.netjob.app.domain.Contrato;
import com.netjob.app.service.dto.ContratoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contrato} and its DTO {@link ContratoDTO}.
 */
@Mapper(componentModel = "spring", uses = { UsuarioMapper.class, ServicioMapper.class })
public interface ContratoMapper extends EntityMapper<ContratoDTO, Contrato> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "id")
    @Mapping(target = "servicio", source = "servicio", qualifiedByName = "id")
    ContratoDTO toDto(Contrato s);
}

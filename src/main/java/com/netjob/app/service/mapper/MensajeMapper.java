package com.netjob.app.service.mapper;

import com.netjob.app.domain.Mensaje;
import com.netjob.app.service.dto.MensajeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Mensaje} and its DTO {@link MensajeDTO}.
 */
@Mapper(componentModel = "spring", uses = { UsuarioMapper.class, ConversacionMapper.class })
public interface MensajeMapper extends EntityMapper<MensajeDTO, Mensaje> {
    @Mapping(target = "emisor", source = "emisor", qualifiedByName = "id")
    @Mapping(target = "receptor", source = "receptor", qualifiedByName = "id")
    @Mapping(target = "conversacion", source = "conversacion", qualifiedByName = "id")
    MensajeDTO toDto(Mensaje s);
}

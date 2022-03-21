package com.netjob.app.service.mapper;

import com.netjob.app.domain.Conversacion;
import com.netjob.app.service.dto.ConversacionDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Conversacion} and its DTO {@link ConversacionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConversacionMapper extends EntityMapper<ConversacionDTO, Conversacion> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ConversacionDTO toDtoId(Conversacion conversacion);

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Set<ConversacionDTO> toDtoIdSet(Set<Conversacion> conversacion);
}

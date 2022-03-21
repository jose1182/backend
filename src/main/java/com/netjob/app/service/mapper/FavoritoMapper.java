package com.netjob.app.service.mapper;

import com.netjob.app.domain.Favorito;
import com.netjob.app.service.dto.FavoritoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Favorito} and its DTO {@link FavoritoDTO}.
 */
@Mapper(componentModel = "spring", uses = { UsuarioMapper.class, ServicioMapper.class })
public interface FavoritoMapper extends EntityMapper<FavoritoDTO, Favorito> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "id")
    @Mapping(target = "servicio", source = "servicio", qualifiedByName = "id")
    FavoritoDTO toDto(Favorito s);
}

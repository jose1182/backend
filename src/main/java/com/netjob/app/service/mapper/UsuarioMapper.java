package com.netjob.app.service.mapper;

import com.netjob.app.domain.Usuario;
import com.netjob.app.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Usuario} and its DTO {@link UsuarioDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface UsuarioMapper extends EntityMapper<UsuarioDTO, Usuario> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    UsuarioDTO toDto(Usuario s);
}

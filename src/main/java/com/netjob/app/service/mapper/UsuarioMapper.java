package com.netjob.app.service.mapper;

import com.netjob.app.domain.Usuario;
import com.netjob.app.service.dto.UsuarioDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Usuario} and its DTO {@link UsuarioDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, ConversacionMapper.class })
public interface UsuarioMapper extends EntityMapper<UsuarioDTO, Usuario> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "conversacions", source = "conversacions", qualifiedByName = "idSet")
    UsuarioDTO toDto(Usuario s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "user", source = "user")
    UsuarioDTO toDtoId(Usuario usuario);

    @Mapping(target = "removeConversacion", ignore = true)
    Usuario toEntity(UsuarioDTO usuarioDTO);
}

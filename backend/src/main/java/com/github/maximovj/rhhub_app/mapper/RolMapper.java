package com.github.maximovj.rhhub_app.mapper;

import java.util.List;
import java.util.Optional;

import com.github.maximovj.rhhub_app.dto.records.RolDTO;
import com.github.maximovj.rhhub_app.dto.request.RolRequest;
import com.github.maximovj.rhhub_app.entity.UsuarioRolEntity;

public class RolMapper {

    public static RolDTO toDTO(UsuarioRolEntity e) {
        return new RolDTO(e);
    }
    
    public static RolDTO toDto(UsuarioRolEntity e) {
        return new RolDTO(e, List.of());
    }

    public static Optional<UsuarioRolEntity> toEntity(RolRequest req) {
        return Optional.of( new UsuarioRolEntity(
            req.getRolId() == null ? null : req.getRolId(), 
            req.getNombre(), 
            req.getDescripcion(), 
            req.getEsAdministrador() == null ? false : req.getEsAdministrador(), 
            req.getEsActivo() == null ? true : req.getEsActivo(), 
            List.of()
        ));
    }

}

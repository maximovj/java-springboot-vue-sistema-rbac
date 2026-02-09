package com.github.maximovj.rhhub_app.dto.records;

import java.util.List;

import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioRolEntity;

public record RolDTO(
    Long rolId,
    String rolNombre,
    String rolDescripcion,
    Boolean rolEsAdministrador,
    Boolean esActivo,
    List<GrupoEntity> grupos) {

        public RolDTO(UsuarioRolEntity e) {
            this(e.getRolId(), e.getRolNombre(), e.getRolDescripcion(), e.getRolEsAdministrador(), e.getEsActivo(), e.getGrupos());
        }

        public RolDTO(UsuarioRolEntity e, List<GrupoEntity> grupos) {
            this(e.getRolId(), e.getRolNombre(), e.getRolDescripcion(), e.getRolEsAdministrador(), e.getEsActivo(), grupos);
        }
    
}

package com.github.maximovj.rhhub_app.dto.records;

import java.util.List;

import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioRolEntity;

public record RolDTO(
    Long rol_id,
    String nombre,
    String descripcion,
    Boolean es_administrador,
    Boolean es_activo,
    List<GrupoEntity> grupos) {

        public RolDTO(UsuarioRolEntity e) {
            this(e.getRolId(), e.getNombre(), e.getDescripcion(), e.getEsAdministrador(), e.getEsActivo(), e.getGrupos());
        }

        public RolDTO(UsuarioRolEntity e, List<GrupoEntity> grupos) {
            this(e.getRolId(), e.getNombre(), e.getDescripcion(), e.getEsAdministrador(), e.getEsActivo(), grupos);
        }
    
}

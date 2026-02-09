package com.github.maximovj.rhhub_app.dto.records;

import java.util.Set;

import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEstadoEntity;
import com.github.maximovj.rhhub_app.entity.GrupoEntity;

public record UsuarioDTO(
    Long usuario_id,
    String usuario,
    String correo,
    Boolean es_activo,
    String token,
    GrupoEntity grupo,
    UsuarioEstadoEntity estado) {

    public UsuarioDTO(UsuarioEntity e) {
        this(e.getUsuarioId(),
            e.getUsuario(),
            e.getCorreo(),
            e.getEsActivo(),
            e.getToken(),
            e.getGrupo(),
            e.getEstado());
    }

    public UsuarioDTO(UsuarioEntity e, GrupoEntity grupo, UsuarioEstadoEntity estado) {
        this(e.getUsuarioId(),
            e.getUsuario(),
            e.getCorreo(),
            e.getEsActivo(),
            e.getToken(),
            grupo,
            estado);
    }

}
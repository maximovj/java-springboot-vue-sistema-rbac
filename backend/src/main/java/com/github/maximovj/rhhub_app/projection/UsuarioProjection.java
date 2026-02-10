package com.github.maximovj.rhhub_app.projection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;

public interface UsuarioProjection {
    
    @JsonProperty("usuario_id")
    Long getUsuarioId();
    
    @JsonProperty("usuario")
    String getUsuario();
    
    @JsonProperty("correo")
    String getCorreo();
    
    @JsonProperty("es_activo")
    Boolean getEsActivo();

    static UsuarioProjection fromEntity(UsuarioEntity e) {
        return new UsuarioProjection() {
            public Long getUsuarioId() { return e.getUsuarioId(); }
            public String getUsuario() { return e.getUsuario(); }
            public String getCorreo() { return e.getCorreo(); }
            public Boolean getEsActivo() { return e.getEsActivo(); }
        };
    }
    
}

package com.github.maximovj.rhhub_app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioFilterRequest {

    private String usuario;

    private String correo;

    private Boolean es_activo;

}

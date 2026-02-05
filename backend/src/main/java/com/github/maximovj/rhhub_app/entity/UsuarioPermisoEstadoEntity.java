package com.github.maximovj.rhhub_app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {})
@EqualsAndHashCode(exclude = {})
@Builder
@Entity
@Table(name = "TBL_USUARIO_PERMISO_ESTADOS")
public class UsuarioPermisoEstadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USUARIO_PERMISO_ESTADO_ID", nullable = false, unique = true)
    @JsonProperty("usuario_permiso_estado_id")
    private Long usuarioPermisoEstadoId;

    @Column(name="ESTADO", nullable = false, unique = true)
    @JsonProperty("estado")
    private String estado;

    @Column(name="DESCRIPCION", nullable = false, unique = true)
    @JsonProperty("descripcion")
    private String descripcion;

    // !! RELACIONES
    
}

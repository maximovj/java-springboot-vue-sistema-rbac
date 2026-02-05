package com.github.maximovj.rhhub_app.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Builder
@ToString(exclude = {"grupo", "estado"})
@EqualsAndHashCode(exclude = {"grupo", "estado"})
@Entity
@Table(
    name = "TBL_USUARIOS", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"USUARIO", "CORREO"})
    }
)
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USUARIO_ID", nullable = false, unique = true)
    @JsonProperty("usuario_id")
    private Long usuarioId;

    @Column(name = "USUARIO", nullable = false, unique = true)
    @JsonProperty("usuario")
    private String usuario;

    @Column(name = "CORREO", nullable = false, unique = true)
    @JsonProperty("correo")
    private String correo;

    @Column(name = "CONTRASENA", nullable = false)
    @JsonProperty("contrasena")
    private String contrasena;

    @Column(name = "ES_ACTIVO", nullable = false)
    @JsonProperty("es_activo")
    private Boolean esActivo;

    @Column(name = "TOKEN")
    @JsonProperty("token")
    private String token;

    // !! RELACIONES

    // Usuario pertenece a 1 grupo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_GRUPO_ID")
    @JsonIgnore
    private UsuarioGruposEntity grupo; 

    // Un usuario tiene un estado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ESTADO_ID")
    @JsonIgnore
    private UsuarioEstadoEntity estado;
}
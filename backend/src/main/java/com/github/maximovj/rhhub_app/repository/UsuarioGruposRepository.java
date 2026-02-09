package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;

public interface UsuarioGruposRepository extends JpaRepository<UsuarioGruposEntity, Long> {

    boolean existsByNombre(String nombre);

    Optional<UsuarioGruposEntity> findByNombre(String nombre);

    @EntityGraph(attributePaths = {"rol", "permisos"})
    @Query("SELECT g FROM UsuarioGruposEntity g WHERE (:grupoId IS NOT NULL AND g.usuarioGrupoId = :grupoId)")
    Optional<UsuarioGruposEntity> qBuscarPorIdRelaciones(
        @Param("grupoId") Long grupoId
    );

}

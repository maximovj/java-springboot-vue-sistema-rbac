package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.maximovj.rhhub_app.entity.GrupoEntity;

public interface GrupoRepository extends JpaRepository<GrupoEntity, Long> {

    boolean existsByNombre(String nombre);

    Optional<GrupoEntity> findByNombre(String nombre);

    @EntityGraph(attributePaths = {"rol", "permisos"})
    @Query("SELECT g FROM GrupoEntity g WHERE (:grupoId IS NOT NULL AND g.grupoId = :grupoId)")
    Optional<GrupoEntity> qBuscarPorIdRelaciones(
        @Param("grupoId") Long grupoId
    );

}

package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;

public interface UsuarioGruposRepository extends JpaRepository<UsuarioGruposEntity, Long> {

    boolean existsByNombre(String nombre);

    Optional<UsuarioGruposEntity> findByNombre(String nombre);

}

package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maximovj.rhhub_app.entity.UsuarioRolEntity;

public interface UsuarioRolRepository extends JpaRepository<UsuarioRolEntity, Long> { 

    boolean existsByRolNombre(String rolNombre);

    Optional<UsuarioRolEntity> findByRolNombre(String rolNombre);

}

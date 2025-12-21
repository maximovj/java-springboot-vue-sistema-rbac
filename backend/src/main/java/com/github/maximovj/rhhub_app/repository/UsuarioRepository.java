package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maximovj.rhhub_app.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    boolean existsByUsuario(String usuario);

    Optional<UsuarioEntity> findByUsuario(String usuario);

    Optional<UsuarioEntity> findByCorreo(String correo);
    
    Optional<UsuarioEntity> findByToken(String token);
    
}
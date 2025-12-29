package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.maximovj.rhhub_app.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    boolean existsByUsuario(String usuario);

    Optional<UsuarioEntity> findByUsuario(String usuario);

    Optional<UsuarioEntity> findByCorreo(String correo);
    
    Optional<UsuarioEntity> findByToken(String token);

    @EntityGraph(attributePaths = {"grupo.rol", "grupo.permisos"})
    @Query("SELECT u FROM UsuarioEntity u WHERE u.usuario = :usuario")
    Optional<UsuarioEntity> findByUsuarioWithDetails(@Param("usuario") String usuario);
    
}
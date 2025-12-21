package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maximovj.rhhub_app.entity.UsuarioPermisosEntity;
import java.util.List;


public interface UsuarioPermisosRepository extends JpaRepository<UsuarioPermisosEntity, Long> {

    boolean existsByPermisoAccion(String permisoAccion);

    Optional<UsuarioPermisosEntity> findByPermisoAccion(String permisoAccion);

    List<UsuarioPermisosEntity> findByPermisoModulo(String permisoModulo);

}

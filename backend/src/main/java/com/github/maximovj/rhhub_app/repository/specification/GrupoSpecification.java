package com.github.maximovj.rhhub_app.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.github.maximovj.rhhub_app.entity.GrupoEntity;

public class GrupoSpecification {

    public static Specification<GrupoEntity> conGrupoId(Long grupoId) {
        return (root, query, cb) ->
                grupoId == null ? null : cb.equal(root.get("grupoId"), grupoId);
    }

    public static Specification<GrupoEntity> conNombre(String nombre) {
        return (root, query, cb) ->
                (nombre == null || nombre.isBlank())
                        ? null
                        : cb.like(
                            cb.lower(root.get("nombre")),
                            "%" + nombre.toLowerCase() + "%"
                        );
    }

    public static Specification<GrupoEntity> conDescripcion(String descripcion) {
        return (root, query, cb) ->
                (descripcion == null || descripcion.isBlank())
                        ? null
                        : cb.like(
                            cb.lower(root.get("descripcion")),
                            "%" + descripcion.toLowerCase() + "%"
                        );
    }

    public static Specification<GrupoEntity> conEsActivo(Boolean esActivo) {
        return (esActivo == null)
                ? null
                : (root, query, cb) -> cb.equal(root.get("esActivo"), esActivo);
    }
    
}

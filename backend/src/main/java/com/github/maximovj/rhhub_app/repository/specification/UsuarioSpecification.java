package com.github.maximovj.rhhub_app.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.dto.request.UsuarioRequest;
import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;

import jakarta.persistence.criteria.Join;

@Component
public class UsuarioSpecification extends BaseSpecification<UsuarioEntity> {
    
    private Specification<UsuarioEntity> build(
            Long usuarioId,
            String usuario,
            String correo,
            Boolean activo,
            Long grupoId
    ) {
        return (root, query, cb) -> {

            if (usuarioId != null) {
                super.spec = super.spec.and(equalsSpec("usuarioId", usuarioId));
            }

            if (usuario != null && !usuario.isBlank()) {
                super.spec = super.spec.and(likeIgnoreCase("usuario", usuario));
            }

            if (correo != null && !correo.isBlank()) {
                super.spec = super.spec.and(likeIgnoreCase("correo", correo));
            }

            if (activo != null) {
                super.spec = super.spec.and(equalsSpec("esActivo", activo));
            }

            if (grupoId != null) {
                super.spec = super.spec.and((r, q, c) -> {
                    q.distinct(true);
                    Join<UsuarioEntity, GrupoEntity> grupo = r.join("grupo");
                    return c.equal(grupo.get("grupoId"), grupoId);
                });
            }
            
            return spec.toPredicate(root, query, cb);
        };
    }

    public Specification<UsuarioEntity> filtro(UsuarioEntity e) {
        return build(
            e.getUsuarioId(),
            e.getUsuario(),
            e.getCorreo(),
            e.getEsActivo(),
            e.getGrupo() != null ? e.getGrupo().getGrupoId() : null
        );
    }

    public Specification<UsuarioEntity> filtro(UsuarioRequest req) {
        return build(
            req.getUsuario_id(),
            req.getUsuario(),
            req.getCorreo(),
            req.getEs_activo(),
            null
        );
    }

}
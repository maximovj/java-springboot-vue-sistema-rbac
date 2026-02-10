package com.github.maximovj.rhhub_app.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.github.maximovj.rhhub_app.entity.UsuarioEntity;

public class UsuarioSpecBuilder extends BaseSpecification<UsuarioEntity> {

    public UsuarioSpecBuilder usuarioId(Long id) {
        if (id != null && id > 0) {
            super.spec = super.spec.and(equalsSpec("usuarioId", id));
        }
        return this;
    }

    public UsuarioSpecBuilder usuario(String usuario) {
        if (usuario != null && !usuario.isBlank()) {
            super.spec = super.spec.and(likeIgnoreCase("usuario", usuario));
        }
        return this;
    }

    public UsuarioSpecBuilder correo(String correo) {
        if (correo != null && !correo.isBlank()) {
            super.spec = super.spec.and(likeIgnoreCase("correo", correo));
        }
        return this;
    }

    public UsuarioSpecBuilder esActivo(Boolean activo) {
        if (activo != null) {
            super.spec = super.spec.and(equalsSpec("esActivo", activo));
        }
        return this;
    }

    public Specification<UsuarioEntity> build() {
        return super.spec;
    }
}

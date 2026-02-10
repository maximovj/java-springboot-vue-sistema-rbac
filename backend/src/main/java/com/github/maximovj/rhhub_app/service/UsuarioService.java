package com.github.maximovj.rhhub_app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.dao.UsuarioDao;
import com.github.maximovj.rhhub_app.dto.request.UsuarioRequest;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.exception.BusinessException;
import com.github.maximovj.rhhub_app.exception.ResourceNotFoundException;
import com.github.maximovj.rhhub_app.projection.UsuarioProjection;
import com.github.maximovj.rhhub_app.repository.UsuarioRepository;
import com.github.maximovj.rhhub_app.repository.specification.UsuarioSpecification;
import com.github.maximovj.rhhub_app.service.integration.BaseServiceImpl;
import com.github.maximovj.rhhub_app.service.integration.JpaBaseRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UsuarioService extends BaseServiceImpl<UsuarioEntity, Long> {

    private final UsuarioRepository usuarioRepository;

    @Override
    protected JpaBaseRepository<UsuarioEntity, Long> getRepository() {
        return this.usuarioRepository;
    }
    
    @Override
    public void delete(Long id) {
        UsuarioEntity usuario = this.usuarioRepository
            .findByUsuarioIdWithRelations(id)
            .orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrada"));

        if(usuario.getGrupo() != null && usuario.getGrupo().getRol() != null) {
            if (usuario.getGrupo().getRol().getEsAdministrador()) {
                throw new BusinessException("No se puede eliminar un SUPER_ADMINISTRADOR");
            }
        }

        super.delete(id);
    }

    public UsuarioEntity findByUsuario(String usuario) {
        return usuarioRepository
            .findByUsuario(usuario).orElse(null);
            //.orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrado"));
    }

    public UsuarioEntity findByUsuarioOrCorreo(String usuario, String correo) {
        return usuarioRepository
            .findByUsuarioOrCorreo(usuario, correo).orElse(null);
            //.orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrado"));
    }

    public boolean existsByUsuarioOrCorreo(String usuario, String correo) {
        return usuarioRepository.existsByUsuarioOrCorreo(usuario, correo);
    }

    public Page<UsuarioProjection> listarUsuarios(Pageable pageable) {
        return this.usuarioRepository.qMostrarUsuarios(pageable);
    }

    public Page<UsuarioProjection> buscarUsuarios(Specification<UsuarioEntity> spec, Pageable pageable) {
        return this.usuarioRepository.qBuscarUsuarios(spec, pageable);
    }

}

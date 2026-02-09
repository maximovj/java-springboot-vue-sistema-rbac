package com.github.maximovj.rhhub_app.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.dto.request.GrupoRequest;
import com.github.maximovj.rhhub_app.dto.response.ApiResponse;
import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.mapper.GrupoMapper;
import com.github.maximovj.rhhub_app.projection.UsuarioProjection;
import com.github.maximovj.rhhub_app.repository.GrupoRepository;
import com.github.maximovj.rhhub_app.repository.specification.GrupoExtendsSpecification;
import com.github.maximovj.rhhub_app.repository.specification.GrupoSpecification;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class GrupoService {

    private final GrupoRepository repo;

    public Page<GrupoEntity> filtraGrupos(
            Long grupoId,
            String nombre,
            String descripcion,
            Boolean esActivo,
            Pageable pageable
    ) {
        Specification<GrupoEntity> spec = Specification
                .where(GrupoSpecification.conGrupoId(grupoId))
                .and(GrupoSpecification.conNombre(nombre))
                .and(GrupoSpecification.conDescripcion(descripcion))
                .and(GrupoSpecification.conEsActivo(esActivo));

        return this.repo.findAll(spec, pageable);
    }

    public ResponseEntity<?> busqueda(Integer page, Integer size, GrupoEntity e) {
        ApiResponse<GrupoEntity> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(
            page, 
            size, 
            Sort.by("nombre").ascending());

        Page<GrupoEntity> pageGrupo = this.filtraGrupos(
                                        e.getGrupoId(), 
                                        e.getNombre(), 
                                        e.getDescripcion(), 
                                        e.getEsActivo(), 
                                        pageable);
                                        
        return response.okPage("Lista de usuarios", pageGrupo);
    }

    public ResponseEntity<?> busquedaX2(Integer page, Integer size, GrupoEntity e) {
        ApiResponse<GrupoEntity> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(
            page, 
            size, 
            Sort.by("nombre").ascending());

        Page<GrupoEntity> pageGrupo = this.repo.findAll(
            new GrupoExtendsSpecification().filtroConEntidad(e), pageable
        );
                                        
        return response.okPage("Lista de usuarios", pageGrupo);
    }

    public ResponseEntity<?> verTodosGrupos() {
        List<GrupoEntity> grupos =  this.repo.findAll();
        return ApiResponse.ok("Lista de grupos", grupos);
    }

    public ResponseEntity<?> verGrupo(Long grupoId) {
        Objects.requireNonNull(grupoId);
        GrupoEntity grupo =  this.repo.qBuscarPorIdRelaciones(grupoId).orElse(null);

        if(grupo == null) { 
            return ApiResponse.notFound("Entidad no encontrada", null);
        }

        return ApiResponse.ok("Info del grupo", GrupoMapper.toDTO(grupo));
    }

    public ResponseEntity<?> actualizarGrupo(Long grupoId, GrupoRequest req) {
        Objects.requireNonNull(grupoId);
        GrupoEntity grupo =  this.repo.findById(grupoId).orElse(null);

        if(grupo == null) { 
            return ApiResponse.notFound("Entidad no encontrada", null);
        }

        if(req.getEsActivo() != null) {
            grupo.setEsActivo(req.getEsActivo());
        }

        if(req.getNombre() != null && !req.getNombre().isBlank()) {
            grupo.setNombre(req.getNombre().trim());
        }

        if(req.getDescripcion() != null && !req.getDescripcion().isBlank()) {
            grupo.setDescripcion(req.getDescripcion().trim());
        }

        GrupoEntity updated = this.repo.save(grupo);
        return ApiResponse.ok("Entidad actualizada correctamente", updated);
    }

    public ResponseEntity<?> crearUnGrupo(GrupoRequest req)
    {
        try {
            Objects.requireNonNull(req.getNombre().trim(), "El campo nombre es obligatoria");
            Objects.requireNonNull(req.getDescripcion().trim(), "El campo descripcion es obligatoria");

            if(this.repo.existsByNombre(req.getNombre().trim())) {
                return ApiResponse.conflict("El nombre de la entidad existe", null);
            }
            
            GrupoEntity entidad = new GrupoEntity();
            entidad.setNombre(req.getNombre());
            entidad.setDescripcion(req.getDescripcion());
            entidad.setEsActivo((req.getEsActivo() == null) ? true : req.getEsActivo());

            GrupoEntity guardado = this.repo.save(entidad);
            return ApiResponse.ok("Entidad creada correctamente", guardado);
        } catch (Exception e) {
            return ApiResponse.internalServerError(e.getMessage(), null);
        }
    }

    public ResponseEntity<?> eliminarUnGrupo(Long grupoId)
    {
        Objects.requireNonNull(grupoId);
        GrupoEntity grupo =  this.repo.findById(grupoId).orElse(null);

        if(grupo == null) { 
            return ApiResponse.notFound("Entidad no encontrada", null);
        }

        this.repo.deleteById(grupoId);
        return ApiResponse.ok("Entidad eliminada correctamente", grupo);
    }
    
}

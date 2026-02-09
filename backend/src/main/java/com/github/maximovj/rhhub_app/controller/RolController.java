package com.github.maximovj.rhhub_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maximovj.rhhub_app.dto.request.RolRequest;
import com.github.maximovj.rhhub_app.service.RolService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
@Slf4j
public class RolController {

    private final RolService service;

    // !! Ver todo los roles
    @GetMapping("")
    public ResponseEntity<?> getVerTodos() {
        return this.service.mostrarTodos();
    }

    // !! Ver detalle de un rol
    @GetMapping("/{rol_id}")
    public ResponseEntity<?> getVerDetalleRol(
        @PathVariable(name = "rol_id") Long rolId
    ) {
        log.info("getVerDetalleRol recibido: {}", rolId);
        return this.service.verDetalleRol(rolId);
    }

    // !! Crear un rol
    @PostMapping("")
    public ResponseEntity<?> postCrearRol(
        @RequestBody @Valid RolRequest req
    ) {
        log.info("postCrearRol recibido: {}", req.toString());
        return this.service.crearRol(req);
    }

    // Actualizar un rol
    @PutMapping("/{rol_id}")
    public ResponseEntity<?> putActualizarRol(
        @PathVariable(name = "rol_id") Long rolId,
        @RequestBody @Valid RolRequest req)
    {
        log.info("putActualizarRol recibido: {}", req.toString());
        return this.service.actualizarRol(rolId, req);
    }

    // Eliminar un rol
    @DeleteMapping("/{rol_id}")
    public ResponseEntity<?> deleteEliminarRol(
        @PathVariable(name = "rol_id") Long rolId
    ) {
        log.info("deleteEliminarRol recibido: {}", rolId);
        return this.service.eliminarUnRol(rolId);
    }
    
}

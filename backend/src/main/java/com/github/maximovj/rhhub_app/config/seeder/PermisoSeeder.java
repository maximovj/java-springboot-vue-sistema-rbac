package com.github.maximovj.rhhub_app.config.seeder;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.config.properties.SeederProperties;
import com.github.maximovj.rhhub_app.entity.UsuarioPermisoEstadoEntity;
import com.github.maximovj.rhhub_app.entity.PermisoEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioPermisoEstadoRepository;
import com.github.maximovj.rhhub_app.repository.PermisoRepository;

import jakarta.transaction.Transactional;

@Profile("seeder")
@Component
@Order(3)
public class PermisoSeeder implements ApplicationRunner {

    @Autowired
    PermisoRepository repository;

    @Autowired
    UsuarioPermisoEstadoRepository usuarioPermisoEstadoRepository;

    @Autowired
    SeederProperties seederProperties;

	@Override
    @Transactional
	public void run(ApplicationArguments args) throws Exception {
		
        if(this.seederProperties.isEnabled() == false) return;
        
        this.permisosModuloPermisos();
        this.permisosModuloUsuarios();
        this.permisosModuloRoles();
	}

    @Transactional
    private void crearUnPermiso(String strPermiso) {
        try {
            if(!repository.existsByPermisoAccion(strPermiso)) {
                Optional<UsuarioPermisoEstadoEntity> estado = usuarioPermisoEstadoRepository.findByEstado("ACTIVO");
                if(estado.isPresent()) {
                    UsuarioPermisoEstadoEntity estado_activo = estado.get();

                    // Crear el objeto de manera más explícita
                    PermisoEntity permiso = PermisoEntity.builder()
                    .permisoAccion(strPermiso)  // Esto es un literal, no es null
                    .permisoModulo("MODULO_USUARIOS")
                    .build();

                    if(permiso != null) {
                        repository.save(permiso);
                        System.out.println("permiso "+strPermiso+" fue creado correctamente.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Hubo un error: " + e.getMessage());
        }
    }

    private void permisosModuloPermisos() {
        String[] permisosModuloPermisos = {
            "VIEW_PERMISOS", 
            "CREATE_PERMISOS", 
            "READ_PERMISOS", 
            "UPDATE_PERMISOS", 
            "DELETE_PERMISOS"};

        for (String string : permisosModuloPermisos) {
            this.crearUnPermiso(string);
        }
    }

    private void permisosModuloUsuarios() {
        String[] permisosModuloUsuarios = {
            "VIEW_USUARIOS", 
            "CREATE_USUARIOS", 
            "READ_USUARIOS", 
            "UPDATE_USUARIOS", 
            "DELETE_USUARIOS"};

        for (String string : permisosModuloUsuarios) {
            this.crearUnPermiso(string);
        }
    }

    private void permisosModuloRoles() {
        String[] permisosModuloRoles = {
            "VIEW_ROLES", 
            "CREATE_ROLES", 
            "READ_ROLES", 
            "UPDATE_ROLES", 
            "DELETE_ROLES"};

        for (String string : permisosModuloRoles) {
            this.crearUnPermiso(string);
        }
    }

}

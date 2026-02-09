package com.github.maximovj.rhhub_app.mapper;

import java.util.Set;

import com.github.maximovj.rhhub_app.dto.records.GrupoDTO;
import com.github.maximovj.rhhub_app.entity.GrupoEntity;

public class GrupoMapper {

    public static GrupoDTO toDTO(GrupoEntity e) {
        return new GrupoDTO(e);
    }

    public static GrupoDTO toDto(GrupoEntity e) {
        return new GrupoDTO(e, null, Set.of());
    }
    
}

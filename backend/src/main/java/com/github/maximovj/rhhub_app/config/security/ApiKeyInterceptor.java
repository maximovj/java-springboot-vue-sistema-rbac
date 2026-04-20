package com.github.maximovj.rhhub_app.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.github.maximovj.rhhub_app.config.properties.AppHeaderProperties;

@Component
@AllArgsConstructor
public class ApiKeyInterceptor implements HandlerInterceptor {

    private final AppHeaderProperties appHeaderProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Solo aplicamos a GET
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            String apiKey = request.getHeader(appHeaderProperties.getKeyName());
            if(Objects.isNull(apiKey)) 
            {
                response.setContentType("text/plain;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Header ApiRbacApp no encontrada");
                return false;
            }

            if (!appHeaderProperties.getKeyValue().equals(apiKey)) {
                response.setContentType("text/plain;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Valor ApiRbacApp no es correcto");
                return false;
            }
        }
        return true; // deja pasar POST, PUT, etc.
    }
}

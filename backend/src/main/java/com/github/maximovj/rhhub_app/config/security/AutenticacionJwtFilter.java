package com.github.maximovj.rhhub_app.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import java.io.IOException;
import java.util.List;

@Component
public class AutenticacionJwtFilter extends OncePerRequestFilter {

    private final ServicioJwt servicioJwt;
    private final UserDetailsServiceImpl servicioDetallesUsuario;

    private static final List<String> URLS_EXCLUIDAS = List.of(
        "/api/v1/autenticacion/login",
        "/api/v1/autenticacion/refresh",
        "/api/v1/autenticacion/logout"
    );

    public AutenticacionJwtFilter(ServicioJwt servicioJwt, UserDetailsServiceImpl servicioDetallesUsuario) {
        this.servicioJwt = servicioJwt;
        this.servicioDetallesUsuario = servicioDetallesUsuario;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return URLS_EXCLUIDAS.contains(path); // si es login/logout, no filtrar
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        String nombreUsuario = null;

        try {
            nombreUsuario = servicioJwt.extraerNombreUsuario(jwt);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expirado");
            return;
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido");
            return;
        }

        if (nombreUsuario != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails detallesUsuario =
                    servicioDetallesUsuario.loadUserByUsername(nombreUsuario);

            if (servicioJwt.esTokenValido(jwt, detallesUsuario)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                detallesUsuario,
                                null,
                                detallesUsuario.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

}
package com.github.maximovj.rhhub_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maximovj.rhhub_app.config.security.ServicioJwt;
import com.github.maximovj.rhhub_app.dto.autenticacion.LoginInDto;
import com.github.maximovj.rhhub_app.dto.autenticacion.LoginOutDto;
import com.github.maximovj.rhhub_app.entity.RenovarTokensEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.repository.RenovarTokensRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioRepository;
import com.github.maximovj.rhhub_app.service.RenovarTokensService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/autenticacion")
public class AutenticacionController {

    @Autowired    
    AuthenticationManager gestorAutenticacion;
    
    @Autowired
    ServicioJwt servicioJwt;
    
    @Autowired
    UserDetailsService servicioDetallesUsuario;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired 
    RenovarTokensRepository renovarTokensRepository;

    @Autowired
    RenovarTokensService renovarTokensService;

    // Login
    @PostMapping("/login")
    public ResponseEntity<LoginOutDto> login(@RequestBody LoginInDto request, HttpServletResponse response) {
        gestorAutenticacion.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getContrasena())
        );

        UserDetails userDetails = servicioDetallesUsuario.loadUserByUsername(request.getUsuario());
        String accessToken = servicioJwt.generarToken(userDetails.getUsername());

        UsuarioEntity usuario = usuarioRepository.findByUsuario(request.getUsuario()).get();
        RenovarTokensEntity refreshToken = renovarTokensService.createRefreshToken(usuario);

        // Guardar refresh token en HttpOnly cookie
        Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/api/v1/autenticacion"); // limitar ruta
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 días
        response.addCookie(cookie);

        return ResponseEntity.ok(new LoginOutDto(accessToken, null));
    }


    // Refresh token
    @PostMapping("/refresh")
    public ResponseEntity<LoginOutDto> refresh(@CookieValue("refreshToken") String refreshTokenValue) {
        RenovarTokensEntity refreshToken = renovarTokensRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if (!renovarTokensService.isValid(refreshToken)) {
            return ResponseEntity.status(401).build();
        }

        String accessToken = servicioJwt.generarToken(refreshToken.getUsuario().getUsuario());
        return ResponseEntity.ok(new LoginOutDto(accessToken, null));
    }


    @PostMapping("/validate")
    public ResponseEntity<Boolean> validarToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String nombreUsuario = servicioJwt.extraerNombreUsuario(token);
                UserDetails detallesUsuario = servicioDetallesUsuario.loadUserByUsername(nombreUsuario);
                return ResponseEntity.ok(servicioJwt.esTokenValido(token, detallesUsuario));
            } catch (Exception e) {
                return ResponseEntity.ok(false);
            }
        }
        return ResponseEntity.ok(false);
    }

    // Logout global
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String usuario) {
        UsuarioEntity usuarioEntity = usuarioRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        renovarTokensService.revokeAllTokens(usuarioEntity);
        return ResponseEntity.ok().build();
    }
    
}
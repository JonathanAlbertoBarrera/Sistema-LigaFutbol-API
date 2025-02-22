package com.sistemaligafutbol.sistemaligafutbol.security.auth;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.UsuarioInactivoException;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import com.sistemaligafutbol.sistemaligafutbol.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest authRequest) {
        // Verificar si el correo existe en la base de datos
        Usuario usuario = usuarioRepository.findByEmail(authRequest.getEmail());

        if (usuario == null) {
            throw new BadCredentialsException("El correo no está registrado");
        }

        if(!usuario.isEstatus()){
            throw new UsuarioInactivoException("El usuario esta inactivo, puedes contactar al administrador para alguna aclaración");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            String token = jwtUtils.createToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String roles = userDetails.getAuthorities()
                    .stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.joining(","));


            return new AuthResponse(token, userDetails.getUsername(), roles,usuario.getId());
        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
            throw new BadCredentialsException("La contraseña es incorrecta");
        }
    }


}

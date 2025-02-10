package com.sistemaligafutbol.sistemaligafutbol.security.auth;

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

        if (usuario == null) { // Evita el NullPointerException
            throw new BadCredentialsException("El correo no está registrado");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            String token = jwtUtils.createToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return new AuthResponse(token, userDetails.getUsername(), userDetails.getAuthorities().toString());
        } catch (org.springframework.security.authentication.BadCredentialsException ex) {
            throw new BadCredentialsException("La contraseña es incorrecta");
        }
    }


}

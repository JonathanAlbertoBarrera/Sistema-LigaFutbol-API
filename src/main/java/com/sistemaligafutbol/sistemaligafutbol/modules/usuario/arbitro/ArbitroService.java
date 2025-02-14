package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro;

import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class ArbitroService {

    @Autowired
    private ArbitroRepository arbitroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Arbitro registrarArbitro(ArbitroRegistroDTO arbitroRegistroDTO) {
        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setEmail(arbitroRegistroDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(arbitroRegistroDTO.getPassword()));
        usuario.setEstatus(true);
        usuario.setRoles(Set.of("ROLE_ARBITRO")); // Asignar el rol de árbitro
        usuarioRepository.save(usuario);

        // Crear el árbitro
        Arbitro arbitro = new Arbitro();
        arbitro.setNombreCompleto(arbitroRegistroDTO.getNombreCompleto());

        arbitro.setUsuario(usuario); // Asociar el usuario
        return arbitroRepository.save(arbitro);
    }
}

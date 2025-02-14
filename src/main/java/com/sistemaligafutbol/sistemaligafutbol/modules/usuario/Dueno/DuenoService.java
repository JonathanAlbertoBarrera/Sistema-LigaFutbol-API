package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno;

import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class DuenoService {

    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Dueno registrarDueno(DuenoRegistroDTO duenoRegistroDTO) {
        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setEmail(duenoRegistroDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(duenoRegistroDTO.getPassword()));
        usuario.setEstatus(true);
        usuario.setRoles(Set.of("ROLE_DUENO")); // Asignar el rol de dueño
        usuarioRepository.save(usuario);

        // Crear el dueño
        Dueno dueno = new Dueno();
        dueno.setNombreCompleto(duenoRegistroDTO.getNombreCompleto());
        dueno.setTelefono(duenoRegistroDTO.getTelefono());
        dueno.setUsuario(usuario); // Asociar el usuario
        return duenoRepository.save(dueno);
    }
}

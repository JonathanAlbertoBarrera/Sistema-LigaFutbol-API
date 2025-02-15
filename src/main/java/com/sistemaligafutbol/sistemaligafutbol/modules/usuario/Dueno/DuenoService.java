package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.imagen.ImgurService;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Service
public class DuenoService {

    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImgurService imgurService;

    @Transactional
    public Dueno registrarDueno(DuenoRegistroDTO duenoRegistroDTO, MultipartFile imagen) {
        try{
            String imagenUrl = (imagen != null && !imagen.isEmpty()) ? imgurService.uploadImage(imagen) : null;
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
            dueno.setImagenUrl(imagenUrl);
            dueno.setUsuario(usuario); // Asociar el usuario
            return duenoRepository.save(dueno);
        }catch(IOException e){
            throw new ImageValidationException("No se pudo procesar la imagen del dueño");
        }
    }
}

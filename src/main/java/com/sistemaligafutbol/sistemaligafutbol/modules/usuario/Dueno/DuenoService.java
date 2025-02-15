package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.modules.imagen.ImgurService;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro.Arbitro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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
            String imagenUrl = imgurService.uploadImage(imagen);
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

    @Transactional
    public Dueno actualizarDueno(Long id, DuenoActualizarDTO duenoactualizarDTO, MultipartFile imagen){
        Dueno dueno = duenoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dueño no encontrado con ID: " + id));

        try{
            // Si se proporciona una nueva imagen, actualizar la URL
            if (imagen != null && !imagen.isEmpty()) {
                String nuevaImagenUrl = imgurService.uploadImage(imagen);
                dueno.setImagenUrl(nuevaImagenUrl);
            }

            //Actualizar datos del dueno
            dueno.setNombreCompleto(duenoactualizarDTO.getNombreCompleto());

            //Actualizar datos del usuario aosciado
            Usuario usuario= dueno.getUsuario();
            usuario.setEmail(duenoactualizarDTO.getEmail());

            usuarioRepository.save(usuario);
            return duenoRepository.save(dueno);

        }catch (IOException e){
            throw new ImageValidationException("No se pudo actualizar la imagen del dueño");
        }
    }

    @Transactional(readOnly = true)
    public List<Dueno> obtenerTodosLosDuenos(){
        return duenoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Dueno obtenerArbitroPorId(Long id){
        return duenoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Dueño no encontrado"));
    }
}

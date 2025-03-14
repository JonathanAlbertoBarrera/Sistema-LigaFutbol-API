package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.arbitro;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.imagen.ImgurService;
import com.sistemaligafutbol.sistemaligafutbol.modules.partido.PartidoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class ArbitroService {

    @Autowired
    private ArbitroRepository arbitroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImgurService imgurService;

    @Transactional
    public Arbitro registrarArbitro(ArbitroDTO arbitroDTO, MultipartFile imagen) {
        try{
            //INTENTAR SUBIR LA IMAGEN
            String imagenUrl=imgurService.uploadImage(imagen);

            // Crear el usuario
            Usuario usuario = new Usuario();
            usuario.setEmail(arbitroDTO.getEmail());
            usuario.setPassword(passwordEncoder.encode(arbitroDTO.getPassword()));
            usuario.setEstatus(true);
            usuario.setRoles(Set.of("ROLE_ARBITRO")); // Asignar el rol de árbitro
            usuarioRepository.save(usuario);

            // Crear el árbitro
            Arbitro arbitro = new Arbitro();
            arbitro.setNombreCompleto(arbitroDTO.getNombreCompleto());
            arbitro.setImagenUrl(imagenUrl);
            arbitro.setUsuario(usuario); // Asociar el usuario
            return arbitroRepository.save(arbitro);
        }catch (IOException e){
            throw new ImageValidationException("No se pudo procesar la imagen del jugador");
        }
    }

    @Transactional
    public Arbitro actualizarArbitro(Long id, ArbitroActualizarDTO arbitroActualizarDTO, MultipartFile imagen) {
        Usuario user = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Buscar el árbitro asociado a este usuario
        Arbitro arbitro = arbitroRepository.findByUsuario(user)
                .orElseThrow(() -> new NotFoundException("El usuario no está asociado a un árbitro"));

        try {
            // Si se proporciona una nueva imagen, actualizar la URL
            if (imagen != null && !imagen.isEmpty()) {
                String nuevaImagenUrl = imgurService.uploadImage(imagen);
                arbitro.setImagenUrl(nuevaImagenUrl);
            }

            // Actualizar datos del árbitro
            arbitro.setNombreCompleto(arbitroActualizarDTO.getNombreCompleto());

            // Actualizar datos del usuario asociado
            Usuario usuario = arbitro.getUsuario();
            usuario.setEmail(arbitroActualizarDTO.getEmail());
            if (arbitroActualizarDTO.getPassword() != null && !arbitroActualizarDTO.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(arbitroActualizarDTO.getPassword()));
            }

            usuarioRepository.save(usuario);
            return arbitroRepository.save(arbitro);
        } catch (IOException e) {
            throw new ImageValidationException("No se pudo actualizar la imagen del árbitro");
        }
    }

    @Transactional
    public String cambiarEstatusArbitro(Long idArbitro) {
        // Buscar el árbitro
        Arbitro arbitro = arbitroRepository.findById(idArbitro)
                .orElseThrow(() -> new NotFoundException("Árbitro no encontrado"));

        // Verificar si tiene partidos pendientes (que aún no han sido jugados)
        boolean tienePartidosPendientes = partidoRepository.existsByArbitroAndJugadoFalse(arbitro);
        if (tienePartidosPendientes) {
            throw new ValidationException("No se puede desactivar el árbitro porque tiene partidos asignados para pitar.");
        }

        // Cambiar el estatus del árbitro
        boolean nuevoEstatus = !arbitro.getUsuario().isEstatus();
        arbitro.getUsuario().setEstatus(nuevoEstatus);
        usuarioRepository.save(arbitro.getUsuario());

        return "Estatus del árbitro actualizado correctamente a " + (nuevoEstatus ? "Activo" : "Inactivo");
    }

    @Transactional(readOnly = true)
    public List<Arbitro> obtenerTodosLosArbitros(){
        return arbitroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Arbitro obtenerArbitroPorId(Long id){
        return arbitroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Árbitro no encontrado"));
    }

    @Transactional
    public Arbitro obtenerArbiPorIdUsuario(Long idUsuario){
        Usuario usuario=usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new NotFoundException("Usuario no encontrado"));
        return arbitroRepository.findByUsuario(usuario)
                .orElseThrow(()-> new NotFoundException("El usuario no está asociado a un árbitro"));
    }
}

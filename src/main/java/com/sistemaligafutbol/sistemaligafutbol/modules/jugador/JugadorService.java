package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import com.sistemaligafutbol.sistemaligafutbol.modules.imagen.ImgurService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class JugadorService {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private ImgurService imgurService;

    @Transactional
    public Jugador crearJugador(JugadorDTO jugadorDTO, MultipartFile imagen) {
        // Validar datos del jugador
        validarDatosJugador(jugadorDTO);

        try {
            // Subir imagen y obtener URL
            String imagenUrl = imgurService.uploadImage(imagen);

            // Crear y configurar nuevo jugador
            Jugador jugador = new Jugador();
            jugador.setNombre(jugadorDTO.getNombre());
            jugador.setApellido(jugadorDTO.getApellido());
            jugador.setImagenUrl(imagenUrl);

            // Guardar y retornar
            return jugadorRepository.save(jugador);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo procesar la imagen del jugador", e);
        }
    }

    public Jugador actualizarJugador(Long id, JugadorDTO jugadorDTO, MultipartFile imagen) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Jugador no encontrado con ID: " + id));

        validarDatosJugador(jugadorDTO);

        try {
            if (imagen != null && !imagen.isEmpty()) {
                String nuevaImagenUrl = imgurService.uploadImage(imagen);
                jugador.setImagenUrl(nuevaImagenUrl);
            }

            jugador.setNombre(jugadorDTO.getNombre());
            jugador.setApellido(jugadorDTO.getApellido());


            return jugadorRepository.save(jugador);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo actualizar la imagen del jugador", e);
        }
    }

    private void validarDatosJugador(JugadorDTO jugadorDTO) {
        if (jugadorDTO.getNombre() == null || jugadorDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador es requerido");
        }
        if (jugadorDTO.getApellido() == null || jugadorDTO.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del jugador es requerido");
        }
    }

    public List<Jugador> obtenerTodosLosJugadores() {
        return jugadorRepository.findAll();
    }

    public Jugador obtenerJugadorPorId(Long id) {
        return jugadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Jugador no encontrado"));
    }

    public void eliminarJugador(Long id) {
        if (!jugadorRepository.existsById(id)) {
            throw new EntityNotFoundException("Jugador no encontrado");
        }
        jugadorRepository.deleteById(id);
    }


}

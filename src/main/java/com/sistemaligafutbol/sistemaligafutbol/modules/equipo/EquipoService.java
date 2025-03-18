package com.sistemaligafutbol.sistemaligafutbol.modules.equipo;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.modules.campo.Campo;
import com.sistemaligafutbol.sistemaligafutbol.modules.campo.CampoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.imagen.GoogleDriveService;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno.Dueno;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno.DuenoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private GoogleDriveService imagenService;

    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CampoRepository campoRepository;

    @Transactional
    public ResponseEntity<EquipoResponseDTO> registrarEquipo(EquipoDTO equipoDTO, MultipartFile imagen) {
        try {
            String imagenUrl = imagenService.uploadImage(imagen);

            Usuario usuario = usuarioRepository.findById(equipoDTO.getIdUsuario())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
            Dueno dueno = duenoRepository.findByUsuario(usuario)
                    .orElseThrow(() -> new NotFoundException("Dueño no encontrado"));
            Campo campo = campoRepository.findById(equipoDTO.getIdCampo())
                    .orElseThrow(() -> new NotFoundException("Campo no encontrado"));

            Equipo equipo = new Equipo();
            equipo.setNombreEquipo(equipoDTO.getNombreEquipo());
            equipo.setLogo(imagenUrl);
            equipo.setDueno(dueno);
            equipo.setCampo(campo);

            Equipo equipoGuardado = equipoRepository.save(equipo);

            return ResponseEntity.status(HttpStatus.CREATED).body(new EquipoResponseDTO(equipoGuardado));

        } catch (IOException e) {
            throw new ImageValidationException("No se pudo procesar la imagen del equipo");
        }
    }


    @Transactional
    public ResponseEntity<EquipoResponseDTO> actualizarEquipo(Long id, EquipoDTO equipoDTO, MultipartFile imagen) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado"));

        Usuario usuario = usuarioRepository.findById(equipoDTO.getIdUsuario())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Dueno dueno = duenoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new NotFoundException("Dueño no encontrado"));

        Campo campo = campoRepository.findById(equipoDTO.getIdCampo())
                .orElseThrow(() -> new NotFoundException("Campo no encontrado"));

        try {
            if (imagen != null && !imagen.isEmpty()) {
                String nuevaImagenUrl = imagenService.uploadImage(imagen);
                equipo.setLogo(nuevaImagenUrl);
            }

            equipo.setNombreEquipo(equipoDTO.getNombreEquipo());
            equipo.setDueno(dueno);
            equipo.setCampo(campo);
            equipoRepository.save(equipo);

            return ResponseEntity.ok(new EquipoResponseDTO(equipo));
        } catch (IOException e) {
            throw new ImageValidationException("No se pudo actualizar la imagen del equipo");
        }
    }


    @Transactional(readOnly = true)
    public List<EquipoResponseDTO> obtenerTodosLosEquipos() {
        return equipoRepository.findAll().stream()
                .map(EquipoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EquipoResponseDTO obtenerEquipoPorId(Long id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado"));
        return new EquipoResponseDTO(equipo);
    }

    @Transactional(readOnly = true)
    public List<EquipoResponseDTO> obtenerEquiposPorDueno(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Dueno dueno = duenoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new NotFoundException("Dueño no encontrado"));

        return equipoRepository.findByDueno(dueno).stream()
                .map(EquipoResponseDTO::new)
                .collect(Collectors.toList());
    }

}

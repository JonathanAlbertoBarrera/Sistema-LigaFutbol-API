package com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Dueno;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.EquipoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.imagen.GoogleDriveService;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.Solicitud;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.SolicitudRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.Usuario;
import com.sistemaligafutbol.sistemaligafutbol.modules.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class DuenoService {

    @Autowired
    private DuenoRepository duenoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GoogleDriveService imagenService;

    @Transactional
    public Dueno registrarDueno(DuenoRegistroDTO duenoRegistroDTO, MultipartFile imagen) {
        try{
            String imagenUrl = imagenService.uploadImage(imagen);
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
                String nuevaImagenUrl = imagenService.uploadImage(imagen);
                dueno.setImagenUrl(nuevaImagenUrl);
            }

            //Actualizar datos del dueno
            dueno.setNombreCompleto(duenoactualizarDTO.getNombreCompleto());

            //Actualizar datos del usuario aosciado
            Usuario usuario= dueno.getUsuario();
            usuario.setEmail(duenoactualizarDTO.getEmail());
            if(duenoactualizarDTO.getPassword() != null && !duenoactualizarDTO.getPassword().isEmpty()){
                usuario.setPassword(passwordEncoder.encode(duenoactualizarDTO.getPassword()));
            }

            usuarioRepository.save(usuario);
            return duenoRepository.save(dueno);

        }catch (IOException e){
            throw new ImageValidationException("No se pudo actualizar la imagen del dueño");
        }
    }

    @Transactional
    public String cambiarEstatusDueno(Long idDueno) {
        Dueno dueno = duenoRepository.findById(idDueno)
                .orElseThrow(() -> new NotFoundException("Dueño no encontrado"));

        Usuario usuario = dueno.getUsuario();
        boolean nuevoEstatus = !usuario.isEstatus(); // Cambia el estatus (activo/inactivo)

        List<Solicitud> solicitudesRechazadas = new ArrayList<>(); // Definir la lista aquí

        if (!nuevoEstatus) { // Si se va a **desactivar**
            boolean tieneEquipoEnTorneoActivo = equipoRepository.findByDueno(dueno).stream()
                    .anyMatch(equipo -> solicitudRepository.existsByEquipoAndResolucionTrueAndInscripcionEstatusTrueAndTorneoEstatusTorneoTrue(equipo));

            if (tieneEquipoEnTorneoActivo) {
                throw new ValidationException("No se puede desactivar al dueño, ya que tiene un equipo participando en un torneo activo.");
            }

            List<Equipo> equiposDelDueno = equipoRepository.findByDueno(dueno);

            // Obtener solicitudes pendientes
            List<Solicitud> solicitudesPendientes = solicitudRepository.findByEquipoInAndPendienteTrue(equiposDelDueno);

            // Obtener solicitudes aceptadas pero sin pagar la inscripción
            List<Solicitud> solicitudesAceptadasSinPago = solicitudRepository.findByEquipoInAndResolucionTrueAndInscripcionEstatusFalse(equiposDelDueno);

            // Rechazar ambas listas de solicitudes
            solicitudesRechazadas.addAll(solicitudesPendientes);
            solicitudesRechazadas.addAll(solicitudesAceptadasSinPago);

            solicitudesRechazadas.forEach(solicitud -> {
                solicitud.setResolucion(false); // Se marca como rechazada
                solicitud.setPendiente(false); // Ya no está en espera
            });

            solicitudRepository.saveAll(solicitudesRechazadas);
        }

        // Cambiar el estatus del usuario
        usuario.setEstatus(nuevoEstatus);
        usuarioRepository.save(usuario);

        return nuevoEstatus
                ? "Dueño activado correctamente."
                : "Dueño desactivado correctamente. Se rechazaron " + solicitudesRechazadas.size() + " solicitudes (pendientes y aceptadas sin pago).";
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

    @Transactional
    public Dueno obtenerDuenoPorIdUsuario(Long idUsuario){
        Usuario usuario=usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new NotFoundException("Usuario no encontrado"));
        return duenoRepository.findByUsuario(usuario)
                .orElseThrow(()-> new NotFoundException("El usuario no está asociado a un dueño"));
    }
}

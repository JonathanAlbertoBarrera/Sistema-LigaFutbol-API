package com.sistemaligafutbol.sistemaligafutbol.modules.jugador;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.Equipo;
import com.sistemaligafutbol.sistemaligafutbol.modules.equipo.EquipoRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.solicitud.SolicitudRepository;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JugadorReportService {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private TorneoRepository torneoRepository;

    public ResponseEntity<byte[]> generarCredencialesPDF(Long idEquipo, Long idTorneo) {
        // Verificar si el equipo existe
        Equipo equipo = equipoRepository.findById(idEquipo)
                .orElseThrow(() -> new NotFoundException("Equipo no encontrado con ID: " + idEquipo));

        // Validar si el equipo está inscrito en el torneo
        boolean estaInscrito = solicitudRepository.existsByEquipoAndTorneo_IdAndResolucionTrueAndInscripcionEstatusTrue(equipo, idTorneo);

        if (!estaInscrito) {
            throw new NotFoundException("El equipo no está confirmado en el torneo indicado");
        }

        // Obtener el torneo
        Torneo torneo = torneoRepository.findById(idTorneo)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado con ID: " + idTorneo));

        // Obtener jugadores habilitados del equipo
        List<Jugador> jugadores = jugadorRepository.findByEquipo_IdAndHabilitadoTrue(idEquipo);

        if (jugadores.isEmpty()) {
            throw new NotFoundException("No hay jugadores habilitados en este equipo.");
        }

        try {
            // Cargar la plantilla JasperReports
            InputStream reportStream = new ClassPathResource("reports/credencial_jugador.jrxml").getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Convertir los jugadores a un formato compatible con Jasper
            List<Map<String, Object>> jugadoresData = jugadores.stream().map(jugador -> {
                Map<String, Object> jugadorMap = new HashMap<>();
                jugadorMap.put("nombreCompleto", jugador.getNombreCompleto());
                jugadorMap.put("fechaNacimiento", convertirLocalDateADate(jugador.getFechaNacimiento())); // Convertimos
                jugadorMap.put("fotoJugador", jugador.getFotoJugador());
                return jugadorMap;
            }).toList();

            // Crear DataSource con los jugadores formateados
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(jugadoresData);

            // Parámetros del reporte
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("LOGO_TORNEO", torneo.getLogoTorneo());
            parameters.put("LOGO_EQUIPO", equipo.getLogo());
            parameters.put("NOMBRE_TORNEO", torneo.getNombreTorneo());
            parameters.put("NOMBRE_EQUIPO", equipo.getNombreEquipo());

            // Generar el PDF
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            // Configurar la respuesta con el PDF generado
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=credenciales_" + equipo.getNombreEquipo() + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar las credenciales", e);
        }
    }

    private Date convertirLocalDateADate(LocalDate localDate) {
        return localDate != null ? java.sql.Date.valueOf(localDate) : null;
    }

}





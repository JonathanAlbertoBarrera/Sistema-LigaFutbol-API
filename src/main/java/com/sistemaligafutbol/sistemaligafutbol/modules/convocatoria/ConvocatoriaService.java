package com.sistemaligafutbol.sistemaligafutbol.modules.convocatoria;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.NotFoundException;
import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ValidationException;
import com.sistemaligafutbol.sistemaligafutbol.modules.imagen.GoogleDriveService;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.Torneo;
import com.sistemaligafutbol.sistemaligafutbol.modules.torneo.TorneoRepository;
import net.sf.jasperreports.engine.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.imageio.ImageIO;

@Service
public class ConvocatoriaService {

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private ConvocatoriaRepository convocatoriaRepository;

    @Autowired
    private GoogleDriveService imgurService;

    @Transactional
    public String publicarConvocatoria(Long torneoId) {
        // Verificar que el torneo existe
        Torneo torneo = torneoRepository.findById(torneoId)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));

        //  Desactivar TODAS las convocatorias antes de publicar la nueva
        convocatoriaRepository.findAll().forEach(c -> {
            c.setPublicada(false);
            convocatoriaRepository.save(c);
        });

        //  Verificar si la convocatoria del torneo ya existe
        Optional<Convocatoria> convocatoriaExistente = convocatoriaRepository.findByTorneo(torneo);

        // Validar que el torneo no esté iniciado
        if (torneo.isIniciado()) {
            throw new ValidationException("No se puede publicar convocatoria para un torneo iniciado.");
        }

        // Generar la imagen de la convocatoria con JasperReports y subirla a Imgur
        String imagenUrl = generarImagenConvocatoria(torneo);

        if (convocatoriaExistente.isPresent()) {
            //  Si la convocatoria ya existe, actualizarla en lugar de crear otra
            Convocatoria convocatoria = convocatoriaExistente.get();
            convocatoria.setFechaInicio(torneo.getFechaInicio());
            convocatoria.setFechaFin(torneo.getFechaFin());
            convocatoria.setMinEquipos(torneo.getMinEquipos());
            convocatoria.setMaxEquipos(torneo.getMaxEquipos());
            convocatoria.setEquiposLiguilla(torneo.getEquiposLiguilla());
            convocatoria.setDescripcion(torneo.getDescripcion());
            convocatoria.setVueltas(torneo.getVueltas());
            convocatoria.setPremio(torneo.getPremio());
            convocatoria.setImagenUrl(imagenUrl);
            convocatoria.setPublicada(true);
            convocatoriaRepository.save(convocatoria);

            return convocatoria.getImagenUrl();
        }

        //  Si no existe una convocatoria, crear una nueva
        Convocatoria convocatoria = new Convocatoria();
        convocatoria.setTorneo(torneo);
        convocatoria.setFechaInicio(torneo.getFechaInicio());
        convocatoria.setFechaFin(torneo.getFechaFin());
        convocatoria.setMinEquipos(torneo.getMinEquipos());
        convocatoria.setMaxEquipos(torneo.getMaxEquipos());
        convocatoria.setEquiposLiguilla(torneo.getEquiposLiguilla());
        convocatoria.setDescripcion(torneo.getDescripcion());
        convocatoria.setVueltas(torneo.getVueltas());
        convocatoria.setPremio(torneo.getPremio());
        convocatoria.setImagenUrl(imagenUrl);
        convocatoria.setPublicada(true);

        Convocatoria publicada = convocatoriaRepository.save(convocatoria);
        return publicada.getImagenUrl();
    }


    private String generarImagenConvocatoria(Torneo torneo) {
        try {
            // Cargar la plantilla de JasperReports
            InputStream reportStream = new ClassPathResource("reports/convocatoria.jrxml").getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Parámetros del reporte
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("LOGO_TORNEO", torneo.getLogoTorneo());
            parameters.put("NOMBRE_TORNEO", torneo.getNombreTorneo());
            parameters.put("FECHA_INICIO", torneo.getFechaInicio().toString());
            parameters.put("FECHA_FIN", torneo.getFechaFin().toString());
            parameters.put("MIN_EQUIPOS", torneo.getMinEquipos());
            parameters.put("MAX_EQUIPOS", torneo.getMaxEquipos());
            parameters.put("EQUIPOS_LIGUILLA", torneo.getEquiposLiguilla());
            parameters.put("VUELTAS", torneo.getVueltas());
            parameters.put("PREMIO", torneo.getPremio());
            parameters.put("DESCRIPCION", torneo.getDescripcion());

            // Generar el reporte en memoria
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            // Exportar el reporte a una imagen PNG usando JasperPrintManager
            BufferedImage bufferedImage = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, 0, 2.0f);

            // Guardar la imagen en un archivo temporal
            File tempFile = File.createTempFile("convocatoria_" + torneo.getId(), ".png");
            ImageIO.write(bufferedImage, "png", tempFile);

            // Validar que el archivo no esté vacío
            if (!tempFile.exists() || tempFile.length() == 0) {
                throw new RuntimeException("Error: La imagen generada está vacía o no existe.");
            } else {
                System.out.println("Tamaño del archivo temporal: " + tempFile.length() + " bytes");
            }

            // Convertir la imagen en MultipartFile y subirla a Imgur
            MultipartFile multipartFile = convertirArchivoAMultipartFile(tempFile);
            if (multipartFile.isEmpty()) {
                throw new RuntimeException("Error: El archivo MultipartFile está vacío.");
            } else {
                System.out.println("Tamaño del MultipartFile: " + multipartFile.getSize() + " bytes");
            }

            String imgurUrl = imgurService.uploadImage(multipartFile);
            return imgurUrl;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar la imagen de la convocatoria", e);
        }
    }


    private MultipartFile convertirArchivoAMultipartFile(File file) throws IOException {
        FileInputStream input = new FileInputStream(file);

        // Crear MockMultipartFile con el contenido del archivo
        return new MockMultipartFile(
                "file",                     // Nombre del parámetro
                file.getName(),              // Nombre del archivo
                "image/png",                 // Tipo de contenido (MIME Type)
                input                        // Contenido del archivo
        );
    }

    @Transactional(readOnly = true)
    public String obtenerConvocatoriaActiva() {
        Convocatoria existente=convocatoriaRepository.findByPublicadaTrue()
                .orElseThrow(() -> new NotFoundException("No hay convocatoria activa"));
        return existente.getImagenUrl();
    }
}




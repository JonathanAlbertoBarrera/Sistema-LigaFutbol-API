package com.sistemaligafutbol.sistemaligafutbol.modules.imagen;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

@Service
public class ImgurService {

    @Value("${imgur.client.id}")
    private String clientId;

    @Value("${imgur.client.secret}")
    private String clientSecret;

    private static final String IMGUR_API_URL = "https://api.imgur.com/3/image";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif"
    );

    public String uploadImage(MultipartFile file) throws IOException {
        validateImage(file);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + clientId);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<ImgurResponse> response = restTemplate.exchange(
                    IMGUR_API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    ImgurResponse.class
            );

            if (response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData().getLink();
            }
            throw new RuntimeException("Error al subir la imagen a Imgur");
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la imagen", e);
        }
    }

    private void validateImage(MultipartFile file) {
        // Validar si el archivo está vacío
        if (file == null || file.isEmpty()) {
            throw new ImageValidationException("El archivo está vacío");
        }

        // Validar el tamaño del archivo
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ImageValidationException(
                    String.format("El tamaño del archivo excede el límite permitido de %d MB", MAX_FILE_SIZE / (1024 * 1024))
            );
        }

        // Validar el tipo de archivo
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new ImageValidationException(
                    "Tipo de archivo no permitido. Solo se permiten imágenes JPEG, PNG y GIF"
            );
        }

        // Validación adicional del contenido real del archivo
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new ImageValidationException("El archivo no es una imagen válida");
            }
        } catch (IOException e) {
            throw new ImageValidationException("No se pudo validar el contenido de la imagen");
        }
    }
}


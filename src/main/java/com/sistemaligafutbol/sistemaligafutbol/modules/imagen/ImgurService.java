package com.sistemaligafutbol.sistemaligafutbol.modules.imagen;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Set;

@Service
public class ImgurService {

    @Value("${imgur.client.id}")
    private String clientId;

    private static final String IMGUR_API_URL = "https://api.imgur.com/3/image";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of("image/jpeg", "image/png", "image/gif");

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

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ImgurResponse> response = restTemplate.exchange(
                IMGUR_API_URL, HttpMethod.POST, new HttpEntity<>(body, headers), ImgurResponse.class
        );

        if (response.getBody() != null && response.getBody().getData() != null) {
            return response.getBody().getData().getLink();
        }
        throw new ImageValidationException("Error al subir la imagen a Imgur");
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ImageValidationException("El archivo está vacío");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ImageValidationException("El tamaño del archivo excede el límite permitido de 5MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new ImageValidationException("Solo se permiten imágenes JPEG, PNG y GIF");
        }

        try {
            if (ImageIO.read(file.getInputStream()) == null) {
                throw new ImageValidationException("El archivo no es una imagen válida");
            }
        } catch (IOException e) {
            throw new ImageValidationException("No se pudo validar el contenido de la imagen");
        }
    }
}



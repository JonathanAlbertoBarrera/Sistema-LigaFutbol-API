package com.sistemaligafutbol.sistemaligafutbol.modules.imagen;

import com.sistemaligafutbol.sistemaligafutbol.exceptions.exception.ImageValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.core.io.ClassPathResource;
import java.io.*;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;

@Service
public class GoogleDriveService {

    @Value("${google.drive.application-name}")
    private String applicationName;

    @Value("${google.drive.credentials-file}")
    private String credentialsFilePath;

    @Value("${google.drive.folder-id}")
    private String folderId;

    private Drive getDriveService() throws IOException {
        InputStream credentialsStream = new ClassPathResource(credentialsFilePath).getInputStream();

        if (credentialsStream == null) {
            throw new FileNotFoundException("⚠️ No se encontró el archivo: " + credentialsFilePath);
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(Collections.singletonList(DriveScopes.DRIVE_FILE));

        return new Drive.Builder(new com.google.api.client.http.javanet.NetHttpTransport(),
                com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(applicationName)
                .build();
    }

    public String uploadImage(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new ImageValidationException("⚠️ La imagen está vacía.");
        }

        Drive driveService = getDriveService();

        // Convertir MultipartFile a File
        File tempFile = File.createTempFile("temp", file.getOriginalFilename());
        file.transferTo(tempFile);
        FileContent mediaContent = new FileContent(file.getContentType(), tempFile);

        // Crear archivo en Google Drive
        com.google.api.services.drive.model.File driveFile = new com.google.api.services.drive.model.File();
        driveFile.setName(file.getOriginalFilename());
        driveFile.setParents(Collections.singletonList(folderId));

        try {
            com.google.api.services.drive.model.File uploadedFile = driveService.files()
                    .create(driveFile, mediaContent)
                    .setFields("id, webViewLink, webContentLink")
                    .execute();

            // Eliminar archivo temporal
            tempFile.delete();

            // Devolver la URL pública
            return "https://drive.google.com/uc?id=" + uploadedFile.getId();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ImageValidationException("⚠️ No se pudo procesar la imagen en Google Drive: " + e.getMessage());
        }
    }
}





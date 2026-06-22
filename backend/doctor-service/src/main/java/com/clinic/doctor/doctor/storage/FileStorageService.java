package com.clinic.doctor.doctor.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {
    private final String uploadDir = "uploads/profiles/";

    public String saveFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("Filename is null");
        }

        String extension = ".jpg";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex).toLowerCase();
        }

        if (!extension.equals(".jpg") && !extension.equals(".jpeg") && !extension.equals(".png")) {
            throw new IOException("Only JPG, JPEG, and PNG extensions are allowed");
        }

        String fileName = UUID.randomUUID() + extension;
        Path path = Paths.get(uploadDir + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        return "/api/doctors/uploads/profiles/" + fileName;
    }
}

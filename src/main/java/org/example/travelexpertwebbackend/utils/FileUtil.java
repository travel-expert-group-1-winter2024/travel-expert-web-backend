package org.example.travelexpertwebbackend.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.example.travelexpertwebbackend.utils.RestUtil.buildPhotoUrl;

public class FileUtil {

    private static final String UPLOAD_DIR = "uploads/";
    
    public static String uploadLocal(MultipartFile image, HttpServletRequest request, String filename) throws IOException {
        Path dirPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        Path filePath = dirPath.resolve(filename);
        Files.write(filePath, image.getBytes());

        return buildPhotoUrl(filename, request);
    }
}

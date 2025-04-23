package org.example.travelexpertwebbackend.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class BlobStorageService {
    private final BlobContainerClient blobContainerClient;

    public BlobStorageService(BlobContainerClient blobContainerClient) {
        this.blobContainerClient = blobContainerClient;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        // Generate unique filename
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();

        // Create a BlobClient for this file
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);

        // Upload the file
        blobClient.upload(file.getInputStream(), file.getSize(), true);

        // Return the blob URL
        return blobClient.getBlobUrl();
    }

    public String uploadFile(MultipartFile file, String fileName) throws IOException {
        // Create a BlobClient for this file
        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);

        // Upload the file
        blobClient.upload(file.getInputStream(), file.getSize(), true);

        // Return the blob URL
        return blobClient.getBlobUrl();
    }
}

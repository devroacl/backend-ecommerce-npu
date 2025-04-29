package com.ecommerce.backendnpu.service;


// Importaciones principales de Google Cloud Storage
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

// Importaciones de Spring y manejo de archivos
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// Importaciones de Java IO
import java.io.IOException;
import java.util.UUID;

// Importación de excepción personalizada
import com.ecommerce.backendnpu.exception.FileStorageException;

// GoogleCloudStorageService.java
@Service
public class GoogleCloudStorageService {

    @Autowired
    private Storage storage;

    @Autowired
    private String bucketName;

    public String uploadFile(MultipartFile file, String fileName) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();
            storage.create(blobInfo, file.getBytes());
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Error al subir el archivo", e);
        }
    }

    public void deleteFile(String fileName) {
        BlobId blobId = BlobId.of(bucketName, fileName);
        storage.delete(blobId);
    }

    public String generateSignedUrl(String imagen) {
    }
}
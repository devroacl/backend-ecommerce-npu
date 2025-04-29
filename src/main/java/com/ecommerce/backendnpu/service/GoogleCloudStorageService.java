package com.ecommerce.backendnpu.service;

import com.ecommerce.backendnpu.exception.FileStorageException;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class GoogleCloudStorageService {

    @Autowired
    private Storage storage;

    @Autowired
    private String bucketName;

    public String uploadFile(MultipartFile file) {
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


    public String generateSignedUrl(String fileName) {
        try {
            BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, fileName)).build();
            return storage.signUrl(
                    blobInfo,
                    7, // Días de validez
                    TimeUnit.DAYS,
                    Storage.SignUrlOption.withV4Signature()
            ).toString();
        } catch (Exception e) {
            throw new FileStorageException("Error generando URL firmada", e);
        }
    }
}
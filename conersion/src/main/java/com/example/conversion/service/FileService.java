package com.example.conversion.service;

import com.example.conversion.entity.UploadedFile;
import com.example.conversion.repository.FileRepository;
import com.example.conversion.util.FileUploadUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FileRepository repo;

    public FileService(FileRepository repo) {
        this.repo = repo;
    }

    public UploadedFile uploadFile(MultipartFile file) throws Exception {

        String cleanName = FileUploadUtils.cleanFilename(file);
        Path dir = Paths.get(uploadDir);

        if (!Files.exists(dir)) Files.createDirectories(dir);

        Path filePath = dir.resolve(cleanName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        UploadedFile uf = new UploadedFile();
        uf.setFileName(cleanName);
        uf.setFilePath(filePath.toString());
        uf.setUploadTime(LocalDateTime.now());

        return repo.save(uf);
    }

    public List<UploadedFile> listFiles() {
        return repo.findAll();
    }
}

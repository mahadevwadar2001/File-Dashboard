package com.example.conversion.service;

import com.example.conversion.entity.UploadedFile;
import com.example.conversion.repository.FileRepository;
import com.example.conversion.util.FileUploadUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FileRepository repo;

    public FileService(FileRepository repo) {
        this.repo = repo;
    }

    /** UPLOAD FILE **/
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

    /** LIST FILES **/
    public List<UploadedFile> listFiles() {
        return repo.findAll();
    }

    /** LOAD FOR DOWNLOAD **/
    public Resource loadAsResource(Long id) throws Exception {
        UploadedFile uf = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        Path path = Paths.get(uf.getFilePath());

        if (!Files.exists(path))
            throw new RuntimeException("File missing on server");

        try {
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error loading file");
        }
    }

    /** DELETE FILE **/
    public boolean deleteById(Long id) {
        Optional<UploadedFile> opt = repo.findById(id);
        if (opt.isEmpty()) return false;

        UploadedFile uf = opt.get();
        Path path = Paths.get(uf.getFilePath());

        try {
            Files.deleteIfExists(path);
            repo.deleteById(id);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

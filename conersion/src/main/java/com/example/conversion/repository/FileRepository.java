package com.example.conversion.repository;

import com.example.conversion.entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<UploadedFile, Long> {}

package com.example.conversion.util;

import java.time.LocalDateTime;

public class FileInfo {
    private String fileName;
    private LocalDateTime uploadTime;

    public FileInfo(String fileName, LocalDateTime uploadTime) {
        this.fileName = fileName;
        this.uploadTime = uploadTime;
    }

    public String getFileName() {
        return fileName;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }
}

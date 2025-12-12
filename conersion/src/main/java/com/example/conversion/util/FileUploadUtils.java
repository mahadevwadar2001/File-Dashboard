package com.example.conversion.util;

import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

public class FileUploadUtils {

    // clean file name
    public static String cleanFilename(MultipartFile file) {
        return file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    // ensure unique filename
    public static String makeUniqueFilename(String fileName) {
        String ext = "";

        int idx = fileName.lastIndexOf(".");
        if (idx >= 0) {
            ext = fileName.substring(idx);
            fileName = fileName.substring(0, idx);
        }

        return fileName + "_" + UUID.randomUUID() + ext;
    }
}

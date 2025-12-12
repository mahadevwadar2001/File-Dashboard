package com.example.conversion.controller;

import com.example.conversion.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileController {

    private final FileService service;

    public FileController(FileService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("files", service.listFiles());
        return "dashboard";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         RedirectAttributes redirectAttributes) {
        try {
            service.uploadFile(file);
            redirectAttributes.addFlashAttribute("message", "Uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Upload failed!");
        }
        return "redirect:/";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws Exception {

        Resource resource = service.loadAsResource(id);
        String filename = resource.getFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {

        boolean deleted = service.deleteById(id);

        if (deleted)
            redirectAttributes.addFlashAttribute("message", "File deleted!");
        else
            redirectAttributes.addFlashAttribute("message", "Error deleting file!");

        return "redirect:/";
    }
}

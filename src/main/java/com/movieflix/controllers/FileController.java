package com.movieflix.controllers;

import com.movieflix.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file")  // Removed trailing slash for consistency
@Slf4j  // Add this annotation for logging
public class FileController {
    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFileHandler(@RequestPart("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }

            String uploadFileName = fileService.uploadFile(path, file);
            return ResponseEntity.ok("File uploaded: " + uploadFileName);
        } catch (IOException e) {
            log.error("Error uploading file: ", e);
            return ResponseEntity.internalServerError()
                    .body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<?> serveFileHandler(@PathVariable String fileName,
                                              HttpServletResponse response) {
        try {
            InputStream resourceFile = fileService.getRessourceFile(path, fileName);
            if (resourceFile == null) {
                return ResponseEntity.notFound().build();
            }
            StreamUtils.copy(resourceFile, response.getOutputStream());
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            log.error("Error serving file: ", e);
            return ResponseEntity.internalServerError()
                    .body("Error serving file: " + e.getMessage());
        }
    }
}

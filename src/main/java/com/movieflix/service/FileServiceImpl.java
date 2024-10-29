package com.movieflix.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        //get the name of the file
        String fileName = file.getOriginalFilename();
        log.info("Uploading file: {} to path: {}", fileName, path);

        // to get the file path
        String filePath = path + File.separator + fileName;
        log.info("Complete file path: {}", filePath);

        //create file object and directory if it doesn't exist
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs(); // use mkdirs() instead of mkdir()
            log.info("Directory created: {}", created);
        }

        // Get absolute path for logging
        log.info("Absolute directory path: {}", directory.getAbsolutePath());

        try {
            // copy the file or upload the file to the path
            Files.copy(file.getInputStream(), Paths.get(filePath));
            // if we do this way we have the probleme of 2 files have the same name
            // Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
            log.info("File successfully copied to destination");
            return fileName;
        } catch (IOException e) {
            log.error("Error during file upload: ", e);
            throw e;
        }
    }

    @Override
    public InputStream getRessourceFile(String path, String filename) throws FileNotFoundException {
        String filePath = path + File.separator + filename;
        log.info("Attempting to read file from: {}", filePath);
        File file = new File(filePath);

        if (!file.exists()) {
            log.error("File not found: {}", filePath);
            throw new FileNotFoundException("File not found: " + filePath);
        }

        return new FileInputStream(filePath);
    }
}
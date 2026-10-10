package com.socialsync.controller;

import com.socialsync.service.MediaStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaController {

    private final MediaStorageService mediaStorageService;

    public MediaController(MediaStorageService mediaStorageService) {
        this.mediaStorageService = mediaStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String relativePath = mediaStorageService.storeFile(file);
        String fullUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(relativePath)
                .toUriString();

        return ResponseEntity.ok(Map.of(
                "url", fullUrl,
                "relativePath", relativePath,
                "fileName", file.getOriginalFilename() != null ? file.getOriginalFilename() : "uploaded_media"
        ));
    }
}

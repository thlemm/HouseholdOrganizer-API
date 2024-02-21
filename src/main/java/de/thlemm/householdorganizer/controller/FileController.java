package de.thlemm.householdorganizer.controller;

import de.thlemm.householdorganizer.controller.response.MessageResponse;
import de.thlemm.householdorganizer.repository.ItemRepository;
import de.thlemm.householdorganizer.service.FileService;
import de.thlemm.householdorganizer.service.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@RestController
@RequestMapping("/api/v2")
public class FileController {
    @Value("${de.thlemm.householdorganizer.files.location}")
    private String rootLocation;
    @Autowired
    FileService fileService;

    @Autowired
    ItemRepository itemRepository;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/upload/{itemId}")
    public ResponseEntity<?> upload(
            @PathVariable("itemId") Long itemId,
            @RequestParam("file") MultipartFile file) {


        if(fileAlreadyExists(file.getOriginalFilename())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("File already exists."));
        }

        if (!itemRepository.existsById(itemId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            fileService.saveFile(file);
        } catch (FileStorageException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(e.getMessage()));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Boolean fileAlreadyExists(String filename) {
        Path directoryPath = Paths.get(rootLocation);
        Path filePath = directoryPath.resolve(filename);

        return Files.exists(filePath);
    }
}

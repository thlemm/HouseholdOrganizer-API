package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.service.FileService;
import de.thlemm.householdorganizer.service.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileServiceImple implements FileService {
    @Value("${de.thlemm.householdorganizer.files.location}")
    private String rootLocation;

    @Value("${de.thlemm.householdorganizer.files.max-size}")
    private int maxFileSize;

    Set<String> imageTypes = Stream.of("image/jpeg", "image/png")
            .collect(Collectors.toCollection(HashSet::new));

    @Override
    public void saveFile(MultipartFile file) throws FileStorageException {

        String filename = file.getOriginalFilename();
        assert filename != null;

        try {
            if (!imageTypes.contains(file.getContentType())) {
                throw new FileStorageException("Filetype not accepted.");
            }
            if (file.getSize() * 0.00000095367432 > maxFileSize) {
                throw new FileStorageException("File too large.");
            }
            Path copyLocation = Paths.get(rootLocation + File.separator + filename);
            Files.copy(file.getInputStream(), copyLocation);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file.");
        }
    }
}

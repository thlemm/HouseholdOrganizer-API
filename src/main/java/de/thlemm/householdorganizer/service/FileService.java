package de.thlemm.householdorganizer.service;

import de.thlemm.householdorganizer.service.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void saveFile(MultipartFile file) throws FileStorageException;
}

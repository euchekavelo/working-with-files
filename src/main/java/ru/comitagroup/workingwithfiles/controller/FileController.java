package ru.comitagroup.workingwithfiles.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.comitagroup.workingwithfiles.dto.ResponseDto;
import ru.comitagroup.workingwithfiles.exception.EmptyFileException;
import ru.comitagroup.workingwithfiles.service.FileService;

import java.io.IOException;

@RequestMapping("/files")
@RestController
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/")
    public ResponseEntity<ResponseDto> uploadFile(@RequestParam("file") MultipartFile file) throws IOException,
            EmptyFileException {

        return ResponseEntity.ok(fileService.uploadFile(file));
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable Integer fileId, HttpServletResponse response)
            throws EmptyFileException {

        return ResponseEntity.ok(fileService.getFile(fileId, response));
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<ResponseDto> deleteFile(@PathVariable Integer fileId) throws EmptyFileException {
        return ResponseEntity.ok(fileService.deleteFile(fileId));
    }

    @GetMapping("/")
    public ResponseEntity<Void> getFiles(HttpServletResponse response) throws IOException {
        fileService.getFiles(response);
        return ResponseEntity.ok().build();
    }
}

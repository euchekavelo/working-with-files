package ru.comitagroup.workingwithfiles.service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.comitagroup.workingwithfiles.dto.ResponseDto;
import ru.comitagroup.workingwithfiles.exception.EmptyFileException;
import ru.comitagroup.workingwithfiles.model.File;
import ru.comitagroup.workingwithfiles.repository.FileRepository;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    @Override
    public ResponseDto uploadFile(MultipartFile file) throws EmptyFileException, IOException {
        if (file.isEmpty()) {
            throw new EmptyFileException("No file was selected for uploading to the server or this file is empty.");
        }

        byte[] bytes = file.getBytes();
        String originalFileName = file.getOriginalFilename();

        File newFile = new File();
        newFile.setFilename(file.getOriginalFilename());
        newFile.setContent(bytes);
        newFile.setSize(file.getSize());
        File savedFileId = fileRepository.save(newFile);

        String text = "The file '" + originalFileName + "' was successfully uploaded to the system server.";
        return getResponseDto(text, savedFileId.getId());
    }

    @Override
    public Resource getFile(Integer fileId, HttpServletResponse response) throws EmptyFileException {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if (optionalFile.isEmpty()) {
            throw new EmptyFileException("File record with specified id not found.");
        }

        File file = optionalFile.get();
        byte[] bytes = file.getContent();
        String fileName = URLEncoder.encode(file.getFilename(), StandardCharsets.UTF_8);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.getType());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");

        return new ByteArrayResource(bytes);
    }

    @Override
    public ResponseDto deleteFile(Integer fileId) throws EmptyFileException {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if (optionalFile.isEmpty()) {
            throw new EmptyFileException("File record with specified id not found.");
        }

        String originalFileName = optionalFile.get().getFilename();
        fileRepository.deleteById(fileId);

        String text = "The file '" + originalFileName + "' has been successfully deleted.";
        return getResponseDto(text, fileId);
    }

    @Override
    public void getFiles(HttpServletResponse response) throws IOException {
        List<File> fileList = fileRepository.findAll();

        try (ServletOutputStream servletOutputStream = response.getOutputStream();
             ZipOutputStream zos = new ZipOutputStream(servletOutputStream)) {
            for (File file : fileList) {
                ZipEntry entry = new ZipEntry(file.getFilename());
                entry.setSize(file.getSize());
                zos.putNextEntry(entry);
                zos.write(file.getContent());
                zos.closeEntry();
            }
        }

        response.setContentType("application/zip");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;");
    }

    private ResponseDto getResponseDto(String message, Integer savedFileId) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(message);
        responseDto.setFileId(savedFileId);

        return responseDto;
    }
}

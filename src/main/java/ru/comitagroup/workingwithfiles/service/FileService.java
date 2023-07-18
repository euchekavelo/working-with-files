package ru.comitagroup.workingwithfiles.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.comitagroup.workingwithfiles.dto.ResponseDto;
import ru.comitagroup.workingwithfiles.exception.EmptyFileException;

import java.io.IOException;

public interface FileService {

    ResponseDto uploadFile(MultipartFile file) throws IOException, EmptyFileException;

    Resource getFile(Integer fileId, HttpServletResponse response) throws EmptyFileException;

    ResponseDto deleteFile(Integer fileId) throws EmptyFileException;

    void getFiles(HttpServletResponse response) throws IOException, EmptyFileException;
}

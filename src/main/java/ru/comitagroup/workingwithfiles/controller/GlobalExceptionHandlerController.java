package ru.comitagroup.workingwithfiles.controller;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.comitagroup.workingwithfiles.dto.ErrorResponseDto;
import ru.comitagroup.workingwithfiles.exception.EmptyFileException;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler({SizeLimitExceededException.class, IOException.class, EmptyFileException.class})
    public ResponseEntity<ErrorResponseDto> handleEmptySearchException(Exception ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setResult(false);
        errorResponseDto.setMessage(ex.getMessage());

        return ResponseEntity.badRequest().body(errorResponseDto);
    }
}

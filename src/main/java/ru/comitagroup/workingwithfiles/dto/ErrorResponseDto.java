package ru.comitagroup.workingwithfiles.dto;

import lombok.Data;

@Data
public class ErrorResponseDto {

    private boolean result;
    private String message;
}

package com.olxapplication.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class ResponseMessageDto {
    private String status;
    private String message;

    @Override
    public String toString() {
        return "ResponseMessageDto{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

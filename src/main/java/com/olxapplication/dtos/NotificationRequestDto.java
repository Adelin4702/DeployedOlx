package com.olxapplication.dtos;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.io.File;

@Getter
@Setter
@AllArgsConstructor
public class NotificationRequestDto {
    private String id;
    private String nume;
    private String email;
    private String action;
    private File file;
}


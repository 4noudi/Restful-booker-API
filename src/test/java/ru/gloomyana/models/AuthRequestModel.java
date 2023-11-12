package ru.gloomyana.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequestModel {
    private String username, password;
}

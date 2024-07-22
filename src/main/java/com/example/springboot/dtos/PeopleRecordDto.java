package com.example.springboot.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PeopleRecordDto(
                @NotBlank String name,
                @NotBlank String cpf,
                @NotNull LocalDate birthDate,
                @NotBlank @Email String email) {
}

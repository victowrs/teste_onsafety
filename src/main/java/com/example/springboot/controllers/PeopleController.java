package com.example.springboot.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.springboot.dtos.PeopleRecordDto;
import com.example.springboot.models.PeopleModel;
import com.example.springboot.repositories.PeopleRepository;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/peoples")
public class PeopleController {

    @Autowired
    private PeopleRepository peopleRepository;

    // Rota para criar uma pessoa
    @PostMapping
    public ResponseEntity<Object> savePeople(@RequestBody @Valid PeopleRecordDto peopleRecordDto) {
        // Validação do CPF se o CPF tiver menos de 11 dígitos
        if (peopleRecordDto.cpf().length() < 11) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("CPF inválido. Deve conter pelo menos 11 dígitos.");
        }

        // Validação do CPF se o CPF tiver mais de 11 dígitos
        if (peopleRecordDto.cpf().length() > 11) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("CPF inválido. Deve conter somente 11 dígitos.");
        }

        try {
            PeopleModel peopleModel = new PeopleModel();
            BeanUtils.copyProperties(peopleRecordDto, peopleModel);
            peopleModel.setCpf(peopleRecordDto.cpf());

            PeopleModel savedPeopleModel = peopleRepository.save(peopleModel);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(savedPeopleModel);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe uma pessoa cadastrada com este CPF.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar a pessoa: " + e.getMessage());
        }
    }

    // Rota para obter todas as pessoas
    @GetMapping
    public ResponseEntity<List<PeopleModel>> getPeoples() {
        List<PeopleModel> peoples = peopleRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(peoples);
    }

    // Rota para obter uma pessoa específica
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOnePeople(@PathVariable(value = "id") Long id) {
        Optional<PeopleModel> optionalPeople = peopleRepository.findById(id);
        if (optionalPeople.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
        }
        return ResponseEntity.status(HttpStatus.OK).body(optionalPeople.get());
    }

    // Rota para atualizar uma pessoa
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePeople(@PathVariable(value = "id") Long id,
            @RequestBody @Valid PeopleRecordDto peopleRecordDto) {
        // Validação do CPF
        if (peopleRecordDto.cpf().length() < 11) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("CPF inválido. Deve conter pelo menos 11 dígitos.");
        }

        // Validação do CPF se o CPF tiver mais de 11 dígitos
        if (peopleRecordDto.cpf().length() > 11) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("CPF inválido. Deve conter somente 11 dígitos.");
        }

        // Verificação da existência da pessoa
        Optional<PeopleModel> optionalPeople = peopleRepository.findById(id);
        if (optionalPeople.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Pessoa não encontrada");
        }

        try {
            // Atualização e salvamento da pessoa
            PeopleModel peopleModelToUpdate = optionalPeople.get();
            BeanUtils.copyProperties(peopleRecordDto, peopleModelToUpdate, "id");
            peopleModelToUpdate.setCpf(peopleRecordDto.cpf());

            PeopleModel updatedPeopleModel = peopleRepository.save(peopleModelToUpdate);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(updatedPeopleModel);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Já existe uma pessoa cadastrada com este CPF.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar a pessoa: " + e.getMessage());
        }
    }

    // Rota para deletar uma pessoa
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePeople(@PathVariable(value = "id") Long id) {
        Optional<PeopleModel> optionalPeople = peopleRepository.findById(id);
        if (optionalPeople.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
        }
        peopleRepository.delete(optionalPeople.get());
        return ResponseEntity.status(HttpStatus.OK).body("Pessoa deletada com sucesso");
    }

}

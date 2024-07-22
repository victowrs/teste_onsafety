package com.example.springboot.repositories;

import com.example.springboot.models.PeopleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<PeopleModel, Long> {

    Optional<PeopleModel> findByCpf(String cpf);

}

package com.colpix.challenge.repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.colpix.challenge.model.Empleado;

public interface EmpleadoRepository extends MongoRepository<Empleado, String> {
    
    List<Empleado> findBySupervisorId(String supervisorId);
    
    @Query(value = "{'supervisorId': ?0}", count = true)
    CompletableFuture<Integer> countBySupervisorId(String supervisorId);
}
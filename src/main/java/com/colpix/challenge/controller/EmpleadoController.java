package com.colpix.challenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.colpix.challenge.model.Empleado;
import com.colpix.challenge.service.EmpleadoService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {
    
    private final EmpleadoService empleadoService;
    
    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }
    
    @PostMapping
    public ResponseEntity<Empleado> saveOrUpdate(@RequestBody Empleado empleado) {
        return ResponseEntity.ok(empleadoService.saveOrUpdate(empleado));
    }
    
    @GetMapping
    public ResponseEntity<List<Empleado>> findAll() {
        return ResponseEntity.ok(empleadoService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) throws ExecutionException, InterruptedException {
        CompletableFuture<Empleado> empleadoFuture = CompletableFuture.supplyAsync(() -> 
            empleadoService.findById(id)
        );
        
        CompletableFuture<Integer> subordinadosFuture = empleadoService.countSubordinados(id.toString());
        
        CompletableFuture.allOf(empleadoFuture, subordinadosFuture).join();
        
        Empleado empleado = empleadoFuture.get();
        Integer subordinados = subordinadosFuture.get();
        
        return ResponseEntity.ok(Map.of(
            "empleado", empleado,
            "subordinados", subordinados
        ));
    }
}
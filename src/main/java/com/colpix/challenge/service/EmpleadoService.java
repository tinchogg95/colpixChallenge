package com.colpix.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.colpix.challenge.model.Empleado;
import com.colpix.challenge.repository.EmpleadoRepository;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class EmpleadoService {
    @Autowired
    private final EmpleadoRepository empleadoRepository;
    
    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }
    
    public Empleado saveOrUpdate(Empleado empleado) {
        if (empleado.getId() != null) {
            return updateEmpleado(empleado);
        } else {
            empleado.setFechaCreacion(new Date());
            empleado.setFechaActualizacion(new Date());
            return empleadoRepository.save(empleado);
        }
       
    }
    private Empleado updateEmpleado(Empleado empleado) {
        return empleadoRepository.findById(empleado.getId())
            .map(existente -> {
                // Copia solo los campos actualizables
                existente.setNombre(empleado.getNombre());
                existente.setEmail(empleado.getEmail());
                existente.setSupervisorId(empleado.getSupervisorId());
                existente.setFechaActualizacion(new Date());
                // Mantiene automáticamente la fechaCreacion original
                return empleadoRepository.save(existente);
            })
            .orElseThrow(() -> new RuntimeException("Empleado con ID " + empleado.getId() + " no encontrado"));
    }
    
    
    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }
    
    public Empleado findById(String id) { 
        return empleadoRepository.findById(id).orElse(null);
    }    
    
    public CompletableFuture<Integer> countSubordinados(String supervisorId) {
        return empleadoRepository.countBySupervisorId(supervisorId);
    }
}
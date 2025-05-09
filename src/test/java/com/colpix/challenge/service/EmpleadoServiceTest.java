package com.colpix.challenge.service;

import com.colpix.challenge.model.Empleado;
import com.colpix.challenge.repository.EmpleadoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmpleadoServiceTest {

    private EmpleadoRepository empleadoRepository;
    private EmpleadoService empleadoService;

    @BeforeEach
    void setUp() {
        empleadoRepository = mock(EmpleadoRepository.class);
        empleadoService = new EmpleadoService(empleadoRepository);
    }

    @Test
    void testSaveOrUpdate() {
        Empleado empleado = new Empleado();
        when(empleadoRepository.save(empleado)).thenReturn(empleado);

        Empleado result = empleadoService.saveOrUpdate(empleado);

        assertEquals(empleado, result);
        verify(empleadoRepository).save(empleado);
    }

    @Test
    void testFindAll() {
        List<Empleado> list = List.of(new Empleado(), new Empleado());
        when(empleadoRepository.findAll()).thenReturn(list);

        List<Empleado> result = empleadoService.findAll();

        assertEquals(2, result.size());
        verify(empleadoRepository).findAll();
    }

    @Test
    void testFindByIdFound() {
        Empleado empleado = new Empleado();
        empleado.setId("1");

        when(empleadoRepository.findById("1")).thenReturn(Optional.of(empleado));

        Empleado result = empleadoService.findById("1");

        assertEquals(empleado, result);
        verify(empleadoRepository).findById("1");
    }

    @Test
    void testFindByIdNotFound() {
        when(empleadoRepository.findById("2")).thenReturn(Optional.empty());

        Empleado result = empleadoService.findById("2");

        assertNull(result);
        verify(empleadoRepository).findById("2");
    }

    @Test
    void testCountSubordinados() throws Exception {
        when(empleadoRepository.countBySupervisorId("123"))
            .thenReturn(CompletableFuture.completedFuture(3));

        CompletableFuture<Integer> resultFuture = empleadoService.countSubordinados("123");

        assertEquals(3, resultFuture.get().intValue());
        verify(empleadoRepository).countBySupervisorId("123");
    }
}

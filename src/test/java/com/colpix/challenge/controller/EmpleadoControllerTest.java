package com.colpix.challenge.controller;

import com.colpix.challenge.model.Empleado;
import com.colpix.challenge.service.EmpleadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmpleadoControllerTest {

    private EmpleadoService empleadoService;
    private EmpleadoController empleadoController;

    @BeforeEach
    void setUp() {
        empleadoService = mock(EmpleadoService.class);
        empleadoController = new EmpleadoController(empleadoService);
    }

    @Test
    void testSaveOrUpdateWithMissingFields() {
        Empleado empleado = new Empleado();
        empleado.setId("123");
        empleado.setNombre(null); // campo faltante
        empleado.setEmail("test@example.com");
        empleado.setSupervisorId("456");

        ResponseEntity<?> response = empleadoController.saveOrUpdate(empleado);

        assertEquals(400, response.getStatusCodeValue());
        assertTrue(((Map<?, ?>) response.getBody()).get("error").toString().contains("Faltan campos obligatorios"));
        verify(empleadoService, never()).saveOrUpdate(any());
    }

    @Test
    void testSaveOrUpdateWithValidEmpleado() {
        Empleado empleado = new Empleado();
        empleado.setId("123");
        empleado.setNombre("Juan");
        empleado.setEmail("juan@example.com");
        empleado.setSupervisorId("456");

        when(empleadoService.saveOrUpdate(any())).thenReturn(empleado);

        ResponseEntity<?> response = empleadoController.saveOrUpdate(empleado);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(empleado, response.getBody());
        verify(empleadoService).saveOrUpdate(empleado);
    }

    @Test
    void testSaveOrUpdateWithoutId() {
        Empleado empleado = new Empleado();
        empleado.setNombre("Juan");
        empleado.setEmail("juan@example.com");
        empleado.setSupervisorId("456");

        when(empleadoService.saveOrUpdate(any())).thenReturn(empleado);

        ResponseEntity<?> response = empleadoController.saveOrUpdate(empleado);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(empleado, response.getBody());
        verify(empleadoService).saveOrUpdate(empleado);
    }

    @Test
    void testFindAll() {
        List<Empleado> empleados = List.of(new Empleado(), new Empleado());
        when(empleadoService.findAll()).thenReturn(empleados);

        ResponseEntity<List<Empleado>> response = empleadoController.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        verify(empleadoService).findAll();
    }

    @Test
    void testFindById() throws Exception {
        String id = "123";
        Empleado empleado = new Empleado();
        empleado.setId(id);
        empleado.setNombre("Maria");

        when(empleadoService.findById(id)).thenReturn(empleado);
        when(empleadoService.countSubordinados(id)).thenReturn(CompletableFuture.completedFuture(5));

        ResponseEntity<?> response = empleadoController.findById(id);

        assertEquals(200, response.getStatusCodeValue());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(empleado, body.get("empleado"));
        assertEquals(5, body.get("subordinados"));
        verify(empleadoService).findById(id);
        verify(empleadoService).countSubordinados(id);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.pruebachain.cuenta.controller;

import com.pruebachain.cuenta.dto.AccountDTO;
import com.pruebachain.cuenta.entities.response.AccountRest;
import com.pruebachain.cuenta.service.impl.CuentaServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author cberm3o
 */
public class CuentaControllerTest {

    @InjectMocks
    CuentaRestController cuentaController;

    @Mock
    CuentaServiceImpl cuentaService;
    
    AccountDTO cuentaDTO;
    
    final String CUENTA_ID = "ASDASDASDASDXAS";

    public CuentaControllerTest() {
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cuentaDTO = new AccountDTO();
        cuentaDTO.setCuenta_id(CUENTA_ID);
        cuentaDTO.setNumero_cuenta("496825");
        cuentaDTO.setTipo_cuenta("AHORROS");
        cuentaDTO.setSaldo_inicial(50.00);
        cuentaDTO.setEstado(Boolean.TRUE);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testGetCuenta() throws Exception {
        Mockito.when(cuentaService.findByAccountId(anyString())).thenReturn(cuentaDTO);
        
        ResponseEntity<AccountRest> ceuntaRest = cuentaController.listarId(CUENTA_ID);
        assertNotNull(ceuntaRest);
        assertEquals(cuentaDTO.getNumero_cuenta(), ceuntaRest.getBody().getNumero_cuenta());
    

    }
}

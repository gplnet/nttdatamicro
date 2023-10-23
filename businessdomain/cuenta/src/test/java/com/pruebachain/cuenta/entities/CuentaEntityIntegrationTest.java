/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.pruebachain.cuenta.entities;

import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 *
 * @author cberm3o
 */
@DataJpaTest
public class CuentaEntityIntegrationTest {
    
    @Autowired
    private TestEntityManager testEntityManager;
    
    public CuentaEntityIntegrationTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    void testCuentaentity_whenValidcuentaDetalisProvided_shouldreturnStoredCuentaDetails(){
        
        Cuenta cuentaEntity = new Cuenta();
        cuentaEntity.setCuenta_id(UUID.randomUUID().toString());
        cuentaEntity.setNumero_cuenta("496825");
        cuentaEntity.setSaldo_inicial(50.00);
        cuentaEntity.setEstado(Boolean.TRUE);
        cuentaEntity.setTipo_cuenta("AHORROS");
        
        Cuenta storedCuentaEntity = testEntityManager.persistAndFlush(cuentaEntity);
        
        Assertions.assertTrue(storedCuentaEntity.getId()>0);
        Assertions.assertEquals(cuentaEntity.getNumero_cuenta(), storedCuentaEntity.getNumero_cuenta());       
        Assertions.assertEquals(cuentaEntity.getTipo_cuenta(), storedCuentaEntity.getTipo_cuenta());
        
        
    }
}

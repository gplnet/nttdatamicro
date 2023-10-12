/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */

package com.pruebachain.cuenta.controller;


import com.pruebachain.cuenta.entities.Cuenta;
import com.pruebachain.cuenta.service.ICuentaService;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author cberm3o
 */
@RestController
@RequestMapping("/cuentas")
public class CuentaRestController {

  @Autowired private ICuentaService cuentaRepository;

  @GetMapping( value = "/listar")
  public  ResponseEntity<List<Cuenta>> findAll() {
    List<Cuenta> cuenta = new ArrayList<>();
    try {
      cuenta = (List<Cuenta>) cuentaRepository.listar();
      if(cuenta == null || cuenta.isEmpty()){
        return ResponseEntity.noContent().build();
      }
    } catch (Exception e) {
      new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
     return new ResponseEntity<List<Cuenta>>((List<Cuenta>) cuenta, HttpStatus.OK);
  }

  @GetMapping(value = "/listar/{id}")
  public ResponseEntity<Cuenta> listarId(@PathVariable Integer id) {
    Cuenta cuenta = new Cuenta();
    try {
      cuenta = cuentaRepository.listarId(id);
    } catch (Exception e) {
      return new ResponseEntity<Cuenta>(cuenta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Cuenta>(cuenta, HttpStatus.OK);	
  }
  
  @GetMapping(value = "/cuentaListar/{cuenta}")
  public ResponseEntity<Cuenta> listarCuenta(@PathVariable String cuenta) {
    Cuenta cuent = new Cuenta();
    try {
      cuent = cuentaRepository.getCuentaByNumber(cuenta);
    } catch (Exception e) {
      return new ResponseEntity<Cuenta>(cuent, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Cuenta>(cuent, HttpStatus.OK);	
  }

  @PutMapping(value = "/actualizar/{id}")
  public ResponseEntity<Integer> put( @RequestBody Cuenta input) {
    int rpsta = 0;
    try {
       rpsta = (int) (cuentaRepository.registrar(input)!= null ? input.getCuenta_id():0);
       //rpsta > 0 ? 0 : 1;
    } catch (Exception e) {
      return new ResponseEntity<Integer>(rpsta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
       return new ResponseEntity<Integer>(rpsta, HttpStatus.OK);
  }

  @PostMapping(value = "/registrar")
  public ResponseEntity<Cuenta> post(@RequestBody Cuenta input) {
    Cuenta cuenta = new Cuenta();
    try {
      cuenta = cuentaRepository.registrar(input);
    } catch (Exception e) {
      return new ResponseEntity<Cuenta>(cuenta, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<Cuenta>(cuenta, HttpStatus.CREATED);
  }

  @DeleteMapping(value = "/eliminar/{id}")
  public ResponseEntity<Integer> delete(@PathVariable Long id) {    
    int resultado = 0;
    try {
      cuentaRepository.eliminar(id);
      resultado = 1;

    } catch (Exception e) {
      resultado = 0;
      return new ResponseEntity<Integer>(resultado, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Integer>(resultado, HttpStatus.OK);
  }
}

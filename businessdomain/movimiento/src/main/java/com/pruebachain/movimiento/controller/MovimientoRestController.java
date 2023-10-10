/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */

package com.pruebachain.movimiento.controller;

import com.pruebachain.movimiento.repository.MovimientoRepository;
import com.pruebachain.movimiento.entities.Movimiento;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author cberm3o
 */
@RestController
@RequestMapping("/movimientos")
public class MovimientoRestController {

  @Autowired 
  private MovimientoRepository movimientoRepository;
  
   private Logger logger = LoggerFactory.getLogger(this.getClass());

  @GetMapping( value = "/listar")
  public ResponseEntity<List<Movimiento>> findAll() {
     List<Movimiento> mov = new ArrayList<>();
    try {
      mov = (List<Movimiento>) movimientoRepository.findAll();
    } catch (Exception e) {
      new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<Movimiento>>((List<Movimiento>) mov, HttpStatus.OK);
  }
  
  
  @GetMapping(value = "/cuenta/all")
  public ResponseEntity<List<Movimiento>> listarByCuenta(@RequestParam String cuenta) {
    logger.info("listarByCuenta"+cuenta);
    List<Movimiento> mov = new ArrayList<>();
    try {
      mov = movimientoRepository.findByNumeroCuenta(cuenta);
      logger.info("listarByCuenta"+mov.toString());
    } catch (Exception e) {
       return new ResponseEntity<List<Movimiento>>(mov, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<Movimiento>>(mov, HttpStatus.OK);
  }

  @GetMapping(value = "/listar/{id}")
  public ResponseEntity<Movimiento> listarId(@PathVariable Long id) {
    Movimiento mov = new Movimiento();
    try {
      mov = movimientoRepository.findById(id).get();
    } catch (Exception e) {
       return new ResponseEntity<Movimiento>(mov, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Movimiento>(mov, HttpStatus.OK);
  }

  @PutMapping(value = "/actualizar/{id}")
  public ResponseEntity<Integer> put(@RequestBody Movimiento input) {
   int rpsta = 0;
    try {
       rpsta = (int) (movimientoRepository.save(input)!= null ? input.getMovimiento_id(): 0);
       //rpsta > 0 ? 0 : 1;
    } catch (Exception e) {
      return new ResponseEntity<Integer>(rpsta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
       return new ResponseEntity<Integer>(rpsta, HttpStatus.OK);
  }

  @PostMapping(value = "/registrar")
  public ResponseEntity<Movimiento> post(@RequestBody Movimiento input) {
     Movimiento mov = new Movimiento();
    try {
      mov = movimientoRepository.save(input);
    } catch (Exception e) {
      return new ResponseEntity<Movimiento>(mov, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Movimiento>(mov, HttpStatus.OK);
  }

 @DeleteMapping(value = "/eliminar/{id}")
  public ResponseEntity<Integer> delete(@PathVariable Long id) {
    int resultado = 0;
    try {
      movimientoRepository.deleteById(id);
      resultado = 1;
    } catch (Exception e) {
      resultado = 0;
      return new ResponseEntity<Integer>(resultado, HttpStatus.INTERNAL_SERVER_ERROR);
      
    }
    return new ResponseEntity<Integer>(resultado, HttpStatus.OK);
  }
}

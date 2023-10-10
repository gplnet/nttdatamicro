/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */

package com.pruebachain.movimiento.repository;

import com.pruebachain.movimiento.entities.Movimiento;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cberm3o
 */

@Repository
public interface MovimientoRepository extends CrudRepository<Movimiento, Long> {

  
  @Query("SELECT t FROM Movimiento t WHERE t.numerocuenta_movimiento = ?1")
  public List<Movimiento> findByNumeroCuenta(String numerocuenta);
  
  
}


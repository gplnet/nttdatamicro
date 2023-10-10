/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */

package com.pruebachain.cliente.repository;

import com.pruebachain.cliente.entities.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cberm3o
 */

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
  
  
  @Query("SELECT c FROM Cliente c WHERE c.cliente_id = ?1")
  public Cliente findByCode(Long code);



}

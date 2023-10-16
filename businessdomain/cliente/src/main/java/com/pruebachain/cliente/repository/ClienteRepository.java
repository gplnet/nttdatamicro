/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */

package com.pruebachain.cliente.repository;

import com.pruebachain.cliente.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

/**
 * @author cberm3o
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

  @Query("SELECT c FROM Cliente c WHERE c.cliente_id = ?1")
  public Cliente findByCode(Long code);
  
  @Query(value = "SELECT * FROM Cliente c INNER JOIN cliente_cuenta cc ON cc.cliente_id = c.cliente_id WHERE cc.cuenta_id = ?1", nativeQuery = true)
  public Object findClienteByCodeAccount(Long code);
  
  @Query(value = "SELECT * FROM Cliente c INNER JOIN cliente_cuenta cc ON cc.cliente_id = c.cliente_id INNER JOIN persona p ON p.cliente_id = c.cliente_id WHERE c.cliente_cod = ?1", nativeQuery = true)
  public Cliente findClienteByAccountId(String code);
  
  



}

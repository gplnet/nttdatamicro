/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */

package com.pruebachain.cuenta.respository;

import com.pruebachain.cuenta.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import org.springframework.stereotype.Repository;

/**
 * @author cberm3o
 */
@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

  @Query(value = "SELECT * FROM cuenta WHERE numero_cuenta = ?1" ,nativeQuery = true)
  public Cuenta getCuentaByNumber(String cuenta);
  
  @Query(value = "SELECT * FROM cuenta WHERE cuenta_id = ?1" ,nativeQuery = true)
  public Cuenta getCuentaByCuentaId(String id);
  
  @Query(value = "SELECT * FROM cuenta c WHERE c.id = ?1" ,nativeQuery = true)
  public Cuenta getCuentaById(Long id);
  
  @Modifying
  @Query(value = "UPDATE cuenta cu SET cu.numero_cuenta = :numero_cuenta, cu.tipo_cuenta= :tipo_cuenta, cu.saldo_inicial=:saldo_inicial WHERE cu.cuenta_id = :id",nativeQuery = true)
  public Cuenta updateCuenta(String id, String numero_cuenta, String tipo_cuenta, double saldo_inicial, boolean estado);

}

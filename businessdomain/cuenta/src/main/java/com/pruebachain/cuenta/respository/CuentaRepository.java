/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */

package com.pruebachain.cuenta.respository;

import com.pruebachain.cuenta.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import org.springframework.stereotype.Repository;

/**
 * @author cberm3o
 */
@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

  @Query(value = "select * from cuenta where numero_cuenta = ?1" ,nativeQuery = true)
	public Cuenta getCuentaByNumber(String cuenta);

}

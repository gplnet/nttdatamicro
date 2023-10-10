/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */

package com.pruebachain.cuenta.respository;

import com.pruebachain.cuenta.entities.Cuenta;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cberm3o
 */

@Repository
public interface CuentaRepository extends CrudRepository<Cuenta, Long> {}

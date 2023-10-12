/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.pruebachain.cuenta.service;

import com.pruebachain.cuenta.entities.Cuenta;
import java.util.List;

/**
 * @author cberm3o
 */
public interface ICuentaService {

  Cuenta registrar(Cuenta cuenta);

  int modificar(Cuenta cuenta);

  void eliminar(Long id);

  Cuenta listarId(int id);

  List<Cuenta> listar();  

  Cuenta getCuentaByNumber(String cuenta);
}

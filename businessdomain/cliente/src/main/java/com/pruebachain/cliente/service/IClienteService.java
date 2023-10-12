/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.pruebachain.cliente.service;

import com.pruebachain.cliente.entities.Cliente;
import java.util.List;


/**
 * @author cberm3o
 */

public interface IClienteService {

  Cliente registrar(Cliente cliente);

  int modificar(Cliente cliente);

  void eliminar(Long id);

  Cliente listarId(int id);
  
  
  Cliente listarByAccount(String cuenta);
  
  Object findClienteByCodeAccount(Long code);

  List<Cliente> listar();
}

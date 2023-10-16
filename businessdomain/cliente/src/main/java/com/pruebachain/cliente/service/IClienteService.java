/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.pruebachain.cliente.service;

import com.pruebachain.cliente.dto.ClienteDTO;

import java.util.List;


/**
 * @author cberm3o
 */

public interface IClienteService {

  ClienteDTO registrar(ClienteDTO cliente);

  ClienteDTO modificar(String clienteID, ClienteDTO cliente);

  void eliminar(String id) throws Exception;

  ClienteDTO listarId(String id) throws Exception;
  
  
  ClienteDTO listarByAccount(String cuenta);
  
  Object findClienteByCodeAccount(Long code);

  List<ClienteDTO> listar();
}

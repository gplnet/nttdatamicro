/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pruebachain.cliente.service.impl;

import com.pruebachain.cliente.entities.Cliente;
import com.pruebachain.cliente.repository.ClienteRepository;
import com.pruebachain.cliente.service.IClienteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cberm3o
 */
@Service
public class ClienteServiceImpl implements IClienteService {

  @Autowired private ClienteRepository repo;

  @Override
  public Cliente registrar(Cliente cliente) {
    return repo.save(cliente);
  }

  @Override
  public int modificar(Cliente cliente) {
    int rspt = 0;
    rspt = (int) (repo.save(cliente) != null ? cliente.getCliente_id():0);
    return rspt > 0 ? 0 :1;
  }

  @Override
  public void eliminar(Long id) {
    repo.deleteById(id);
  }

  @Override
  public Cliente listarId(int id) {
    return (Cliente) repo.findById(Long.valueOf(id)).get();
  }

  @Override
  public List<Cliente> listar() {
    return repo.findAll();
  }

  @Override
  public Cliente listarByAccount(String cuenta) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public Object findClienteByCodeAccount(Long code) {
    return repo.findClienteByCodeAccount(code);
  }
}

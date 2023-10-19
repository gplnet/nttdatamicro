/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pruebachain.cliente.service.impl;

import com.pruebachain.cliente.dto.ClienteDTO;
import com.pruebachain.cliente.entities.Cliente;
import com.pruebachain.cliente.entities.response.ErrorMessages;
import com.pruebachain.cliente.repository.ClienteRepository;
import com.pruebachain.cliente.service.IClienteService;
import com.pruebachain.cliente.util.Utils;
import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cberm3o
 */
@Service
public class ClienteServiceImpl implements IClienteService {

  @Autowired private ClienteRepository repo;
  @Autowired private Utils utils;

  @Override
  public ClienteDTO registrar(ClienteDTO cliente) {
    ModelMapper modelMapper = new ModelMapper();  
    cliente.setCliente_cod(utils.generateClienteId(30));
    Cliente client = modelMapper.map(cliente, Cliente.class);
    //BeanUtils.copyProperties(cliente, client);
    cliente.getCuentas().forEach(x-> x.setCliente(client));    
    Cliente storedClient = repo.save(client);
    
    ClienteDTO returnValue =  modelMapper.map(storedClient, ClienteDTO.class);
    //BeanUtils.copyProperties(storedClient, returnValue);
    
    return returnValue; 
  }

  @Override
  public ClienteDTO modificar(String clienteID, ClienteDTO cliente) {
    ClienteDTO returnValue = new ClienteDTO();
    Cliente storedClient = repo.findClienteByAccountId(clienteID);
    storedClient.setNombre(cliente.getNombre());
    storedClient.setDireccion(cliente.getDireccion());
    storedClient.setTelefono(cliente.getTelefono());
    storedClient.setContrasena(cliente.getContrasena());
    storedClient.setEstado(cliente.getEstado());
    storedClient.setCuentas(cliente.getCuentas());
    Cliente updatedCliente = repo.save(storedClient);
    BeanUtils.copyProperties(updatedCliente, returnValue);
    return returnValue;
  }

  @Override
  public void eliminar(String id) throws Exception{
    Cliente storedClient = repo.findClienteByAccountId(id);
    if (storedClient == null) {
        throw new Exception(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
    }
    repo.deleteById(Long.valueOf(storedClient.getCliente_id()));
  }

  @Override
  public ClienteDTO listarId(String id)  throws Exception{
    ClienteDTO returnValue = new ClienteDTO();
    Cliente storedCliente = repo.findClienteByAccountId(id);
    if (storedCliente == null) {
            throw new Exception(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
    BeanUtils.copyProperties(storedCliente, returnValue);
    return returnValue;
  }

  @Override
  public List<ClienteDTO> listar() {
    List<ClienteDTO> returnValueList = new ArrayList<>();
    List<Cliente> listaModel = repo.findAll();
    for(Cliente client : listaModel){
        ClienteDTO returnValue = new ClienteDTO();
        BeanUtils.copyProperties(client, returnValue);
        returnValueList.add(returnValue);
    }
    return returnValueList;
  }

  @Override
  public ClienteDTO listarByAccount(String cuenta) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public Object findClienteByCodeAccount(Long code) {
    return repo.findClienteByCodeAccount(code);
  }

    @Override
    public List<Long> findClienteByName(String nombre) {
        return repo.findClienteByName(nombre);
    }
}

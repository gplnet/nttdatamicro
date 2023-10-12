/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pruebachain.cuenta.service.impl;

import com.pruebachain.cuenta.entities.Cuenta;
import com.pruebachain.cuenta.respository.CuentaRepository;
import com.pruebachain.cuenta.service.ICuentaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cberm3o
 */
@Service
public class CuentaServiceImpl implements ICuentaService {
  
  @Autowired
	private CuentaRepository dao;

  @Override
  public Cuenta registrar(Cuenta cuenta) {
    return dao.save(cuenta);
  }

  @Override
  public int modificar(Cuenta cuenta) {
    int rpst = (int) (dao.save(cuenta) != null ? cuenta.getCuenta_id() : 0);
		return rpst > 0 ? 1 : 0;
  }

  @Override
  public void eliminar(Long id) {
     dao.deleteById(id);
  }

  @Override
  public Cuenta listarId(int id) {
    return dao.findById(Long.valueOf(id)).get();
  }

  @Override
  public List<Cuenta> listar() {
    return dao.findAll();
  }

  @Override
  public Cuenta getCuentaByNumber(String cuenta) {    
    return dao.getCuentaByNumber(cuenta);
  }
}

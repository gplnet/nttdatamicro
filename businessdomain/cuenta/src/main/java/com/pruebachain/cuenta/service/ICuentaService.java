/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.pruebachain.cuenta.service;

import com.pruebachain.cuenta.dto.AccountDTO;
import com.pruebachain.cuenta.entities.Cuenta;
import java.util.List;

/**
 * @author cberm3o
 */
public interface ICuentaService {

  AccountDTO registrar(AccountDTO cuenta);
  
  AccountDTO updateAcount(String accountId, AccountDTO cuenta);
  
  AccountDTO findByAccountId(String id) throws Exception; 

  void eliminar(String id) throws Exception;;  

  List<AccountDTO> listar();  

  AccountDTO getCuentaByNumber(String cuenta)throws Exception;
  Long getIdByNumberAccount(String cuenta)throws Exception;
  
  AccountDTO getCuentaById(Long id);
}

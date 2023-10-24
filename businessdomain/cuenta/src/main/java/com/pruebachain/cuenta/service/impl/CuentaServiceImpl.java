/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pruebachain.cuenta.service.impl;

import com.pruebachain.cuenta.dto.AccountDTO;
import com.pruebachain.cuenta.entities.Cuenta;
import com.pruebachain.cuenta.entities.response.ErrorMessages;
import com.pruebachain.cuenta.respository.CuentaRepository;
import com.pruebachain.cuenta.service.ICuentaService;
import com.pruebachain.cuenta.util.Utils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cberm3o
 */
@Service
public class CuentaServiceImpl implements ICuentaService {

    @Autowired
    private CuentaRepository dao;
    @Autowired
    private Utils utils;

    @Override
    public void eliminar(String id) throws Exception {
        Cuenta storedAccount = dao.getCuentaByCuentaId(id);
        if (storedAccount == null) {
            throw new Exception(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        dao.deleteById(storedAccount.getId());
    }

    @Override
    public List<AccountDTO> listar() {
        List<AccountDTO> returnValueList = new ArrayList<>();
        List<Cuenta> listaModel = dao.findAll();
        for (Cuenta account : listaModel) {
            AccountDTO returnValue = new AccountDTO();
            BeanUtils.copyProperties(account, returnValue);
            returnValueList.add(returnValue);
        }
        return returnValueList;
    }

    @Override
    public AccountDTO registrar(AccountDTO cuenta) {

        cuenta.setCuenta_id(utils.generateCuentaId(30));

        Cuenta account = new Cuenta();
        BeanUtils.copyProperties(cuenta, account);

        Cuenta storedAccount = dao.save(account);

        AccountDTO returnValue = new AccountDTO();
        BeanUtils.copyProperties(storedAccount, returnValue);

        return returnValue;
    }

    @Override
    public AccountDTO updateAcount(String accountId, AccountDTO cuenta) {
        AccountDTO returnValue = new AccountDTO();
        Cuenta storedAccount = dao.getCuentaByCuentaId(accountId);

        storedAccount.setNumero_cuenta(cuenta.getNumero_cuenta());
        storedAccount.setTipo_cuenta(cuenta.getTipo_cuenta());
        storedAccount.setSaldo_inicial(cuenta.getSaldo_inicial());
        storedAccount.setEstado(cuenta.getEstado());

        Cuenta updatedCuenta = dao.save(storedAccount);
        BeanUtils.copyProperties(updatedCuenta, returnValue);
        return returnValue;
    }

    @Override
    public AccountDTO getCuentaByNumber(String cuenta)throws Exception {
        AccountDTO returnValue = new AccountDTO();
        Cuenta storedAccount = dao.getCuentaByNumber(cuenta);
        if (storedAccount == null) {
            throw new Exception(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        BeanUtils.copyProperties(storedAccount, returnValue);
        return returnValue;    }

    @Override
    public AccountDTO findByAccountId(String id) throws Exception {
        AccountDTO returnValue = new AccountDTO();
        Cuenta storedAccount = dao.getCuentaByCuentaId(id);
        if (storedAccount == null) {
            throw new Exception(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        BeanUtils.copyProperties(storedAccount, returnValue);
        return returnValue;
    }

    @Override
    public AccountDTO getCuentaById(Long id) {
        AccountDTO returnValue = new AccountDTO();
        Cuenta storedAccount = dao.getCuentaById(id);
        BeanUtils.copyProperties(storedAccount, returnValue);
        return returnValue;
    }

    @Override
    public Long getIdByNumberAccount(String cuenta) throws Exception {
        return dao.getIdByNumberAccount(cuenta); 
    }
    

}

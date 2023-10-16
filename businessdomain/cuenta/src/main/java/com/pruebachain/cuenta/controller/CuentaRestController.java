/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.pruebachain.cuenta.controller;

import com.pruebachain.cuenta.dto.AccountDTO;
import com.pruebachain.cuenta.entities.Cuenta;
import com.pruebachain.cuenta.entities.request.AcccountRequestModel;
import com.pruebachain.cuenta.entities.response.AccountRest;
import com.pruebachain.cuenta.entities.response.OperationStatusModel;
import com.pruebachain.cuenta.entities.response.OperationsName;
import com.pruebachain.cuenta.entities.response.RequestOperationStatus;
import com.pruebachain.cuenta.service.ICuentaService;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author cberm3o
 */
@RestController
@RequestMapping("/cuentas")
public class CuentaRestController {

    @Autowired
    private ICuentaService cuentaRepository;

    @GetMapping(value = "/cuentaListar/{cuenta}")
    public ResponseEntity<AccountRest> listarCuenta(@PathVariable String cuenta) {
        AccountRest cuent = new AccountRest();
        try {
            ModelMapper modelMapper = new ModelMapper();
            AccountDTO createdAccount = cuentaRepository.getCuentaByNumber(cuenta);
            cuent = modelMapper.map(createdAccount, AccountRest.class);
        } catch (Exception e) {
            return new ResponseEntity<AccountRest>(cuent, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<AccountRest>(cuent, HttpStatus.OK);
    }
    
     

    @PutMapping(value = "/actualizar/{id}")
    public ResponseEntity<OperationStatusModel> put(@PathVariable String id, @RequestBody AcccountRequestModel input) {        
        OperationStatusModel returnValue = new OperationStatusModel();
        try {            
            ModelMapper modelMapper = new ModelMapper();
            AccountDTO accountDto = modelMapper.map(input, AccountDTO.class);
            returnValue.setOpretationName(OperationsName.UPDATE.name());
            AccountDTO createdAccount = cuentaRepository.updateAcount(id, accountDto);   
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name()); 
        } catch (Exception e) {
            return new ResponseEntity<OperationStatusModel>(returnValue, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<OperationStatusModel>(returnValue, HttpStatus.OK);
    }

    @DeleteMapping(value = "/eliminar/{id}")
    public ResponseEntity<OperationStatusModel> delete(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        try {

            returnValue.setOpretationName(OperationsName.DELETE.name());
            cuentaRepository.eliminar(id);
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        } catch (Exception e) {

            return new ResponseEntity<OperationStatusModel>(returnValue, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<OperationStatusModel>(returnValue, HttpStatus.OK);
    }

    @GetMapping(value = "/listar")
    public ResponseEntity<List<AccountRest>> findAll() {
        List<AccountRest> cuentaList = new ArrayList<>();
        try {
            
            List<AccountDTO> listDTO =  cuentaRepository.listar();
            for(AccountDTO  accounDto : listDTO){                
                ModelMapper modelMapper = new ModelMapper();
                AccountRest accountRest = modelMapper.map(accounDto, AccountRest.class);
                cuentaList.add(accountRest);
            }
            
        } catch (Exception e) {
            new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<AccountRest>>((List<AccountRest>) cuentaList, HttpStatus.OK);
    }

    @GetMapping(value = "/listar/{id}")
    public ResponseEntity<AccountRest> listarId(@PathVariable String id) {
        AccountRest cuenta = new AccountRest();
        try {
            ModelMapper modelMapper = new ModelMapper();
            AccountDTO createdAccount = cuentaRepository.findByAccountId(id);
            cuenta = modelMapper.map(createdAccount, AccountRest.class);
        } catch (Exception e) {
            return new ResponseEntity<AccountRest>(cuenta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<AccountRest>(cuenta, HttpStatus.OK);
    }

    @PostMapping(value = "/registrar")
    public ResponseEntity<AccountRest> post(@RequestBody AcccountRequestModel input) {

        AccountRest cuenta = new AccountRest();
        try {

            ModelMapper modelMapper = new ModelMapper();
            AccountDTO accountDto = modelMapper.map(input, AccountDTO.class);

            AccountDTO createdAccount = cuentaRepository.registrar(accountDto);
            cuenta = modelMapper.map(createdAccount, AccountRest.class);

        } catch (Exception e) {
            return new ResponseEntity<AccountRest>(cuenta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<AccountRest>(cuenta, HttpStatus.CREATED);
    }

}

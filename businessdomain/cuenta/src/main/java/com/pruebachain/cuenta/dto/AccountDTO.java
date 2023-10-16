/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pruebachain.cuenta.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author cberm3o
 */
@Data
public class AccountDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long id;
  private String cuenta_id;
  private String numero_cuenta;
  private String tipo_cuenta;
  private double saldo_inicial;
  private Boolean estado;
  
  
}

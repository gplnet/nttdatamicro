/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pruebachain.cuenta.entities.request;

import lombok.Data;

/**
 * @author cberm3o
 */
@Data
public class AcccountRequestModel {

  private String numero_cuenta;
  private String tipo_cuenta; 
  private double saldo_inicial;  
  private Boolean estado;



}

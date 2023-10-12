/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pruebachain.movimiento.util;

import lombok.Data;

/**
 * @author cberm3o
 */
@Data
public class MovimientoReporte {
  
  private String fecha;
	private String cliente;
	private String numero_cuenta;
	private String tipo;
	private double saldo_inicial;
	private Boolean estado;
	private String movimiento;	
	private double saldo_disponible;

}

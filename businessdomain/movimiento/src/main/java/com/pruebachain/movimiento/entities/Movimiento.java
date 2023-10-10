/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pruebachain.movimiento.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

/**
 * @author cberm3o
 */
@Entity
@Data
public class Movimiento implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long movimiento_id;
  
  @JsonSerialize(using = ToStringSerializer.class)
	private LocalDateTime fecha;
  
  @Column(name = "tipo_movimiento", length = 50, nullable = false)
	private String tipo_movimiento;
  
  
  @Column(name = "valor_movimiento", columnDefinition = "Decimal(10,2)", nullable = true)
	private double valor_movimiento;
  
  @Column(name = "saldo_movimiento", columnDefinition = "Decimal(7,2)", nullable = true)
	private double saldo_movimiento;
  
  
  @Column(name  = "numerocuenta_movimiento", length = 150, nullable = false)
  private String numerocuenta_movimiento;
  

}

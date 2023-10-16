/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pruebachain.cuenta.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

/**
 * @author cberm3o
 */
@Entity
@Data
public class Cuenta implements Serializable {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(name  = "cuenta_id", nullable = false)
  private String cuenta_id;
  
  @Column(name  = "numero_cuenta", length = 150, nullable = false, unique = true)
  private String numero_cuenta;
  
  @Column(name  = "tipo_cuenta", length = 150, nullable = false)
  private String tipo_cuenta; 
  
  @Column(name = "saldo_inicial", columnDefinition = "Decimal(10,2)", nullable = false)
  private double saldo_inicial;
  
  @Column(name = "estado")
  private Boolean estado = true;
  
  //@ManyToOne
	//@JoinColumn(name="cliente_id", nullable = false)
	//private Cliente cliente;

}

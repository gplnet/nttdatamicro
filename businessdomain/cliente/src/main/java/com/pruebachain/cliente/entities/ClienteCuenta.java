/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pruebachain.cliente.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import lombok.Data;

/**
 * @author cberm3o
 */

@Entity
@Data
public class ClienteCuenta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  
  private long cuenta_id;
  @Transient 
  private String cuenta_tipo;
  @Transient 
  private String numero_cuenta;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY, targetEntity = Cliente.class)
  @JoinColumn(name = "cliente_id", nullable = true)
  private Cliente cliente;
}

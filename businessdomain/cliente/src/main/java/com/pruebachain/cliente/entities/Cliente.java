/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pruebachain.cliente.entities;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;


import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

import lombok.Data;

/**
 * @author cberm3o
 */

@Entity
@Data
@PrimaryKeyJoinColumn(name="cliente_id")
public class Cliente extends Persona {

  
  private Long cliente_id;
  
  @Column(name  = "contrasena", length = 150, nullable = false)
  private String  contrasena;
  @Column(name = "estado", nullable=true, columnDefinition = "boolean default true")
  private Boolean  estado;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ClienteCuenta> cuentas;

  @Transient 
  private List<?> movientos;
  

  public Cliente(List<ClienteCuenta> cuentas, List<?> movientos) {
    this.cuentas =  new ArrayList<ClienteCuenta>();
    this.movientos = new ArrayList<>();
    
  }

  public Cliente() {
    
  }
  
  
  
}

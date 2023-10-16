/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pruebachain.cliente.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import lombok.Data;

/**
 * @author cberm3o
 */
@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Persona {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cliente_id;
  @Column(name  = "nombre", length = 150, nullable = false)
  private String  nombre;
  @Column(name = "genero", length = 1, nullable = true)
  private String  genero;
  @Column(name = "edad", length = 9, nullable=true)
  private int     edad;
  @Column(name  = "identificacion", length = 20, nullable = false)
  private String  identificacion;
  @Column(name  = "direccion", length = 150, nullable = false)
  private String  direccion;
  @Column(name  = "telefono", length = 150, nullable = true)
  private String telefono;
  
  
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pruebachain.cliente.entities.request;

import com.pruebachain.cliente.entities.ClienteCuenta;
import java.util.List;
import lombok.Data;

/**
 *
 * @author cberm3o
 */
@Data
public class ClientRequestModel {
    
    private String  contrasena;
    private Boolean  estado;
    
    private String  nombre;
    private String  genero;
    private int     edad;
    private String  identificacion;
    private String  direccion;
    private String telefono;
    
    private List<ClienteCuenta> cuentas;    
    private List<?> movientos;
    
}

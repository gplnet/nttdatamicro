/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pruebachain.cliente.dto;

import com.pruebachain.cliente.entities.ClienteCuenta;
import java.util.List;
import lombok.Data;

/**
 *
 * @author cberm3o
 */
@Data
public class ClienteDTO {
    
   
    private String cliente_id;
    private String cliente_cod;
    private String  identificacion;
    private String nombre;
    private String direccion;
    private String telefono;
    private String contrasena;
    private Boolean  estado;
    private String  genero;
    private int     edad;
    

    private List<ClienteCuenta> cuentas;
    private List<?> movientos;
    
}

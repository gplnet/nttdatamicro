/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pruebachain.cliente.entities.response;

import com.pruebachain.cliente.entities.ClienteCuenta;
import java.util.List;
import lombok.Data;

/**
 *
 * @author cberm3o
 */
@Data
public class ClientRest {

    
    private String identificacion;
    private String nombre;
    private String genero;
    private String direccion;
    private String telefono;
    private String contrasena;
    private Boolean  estado;

    

    private List<ClienteCuenta> cuentas;
    private List<?> movientos;

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pruebachain.cliente.dto;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author cberm3o
 */
@Data
public class ClienteCuentaDTO  implements Serializable{
    private String numero_cuenta;
    private long cuenta_id;
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pruebachain.movimiento.entities.response;

import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author cberm3o
 */
@Data
public class MoviemientoRestDate {
    
    private String movimiento_id;
    private LocalDateTime fecha;
    private String tipo_movimiento;
    private double valor_movimiento;
    private double saldo_movimiento;
    private String numerocuenta_movimiento;
    
}

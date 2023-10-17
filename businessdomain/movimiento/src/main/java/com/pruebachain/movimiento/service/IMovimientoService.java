/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.pruebachain.movimiento.service;

import com.pruebachain.movimiento.dto.MovimientoDTO;
import com.pruebachain.movimiento.entities.Movimiento;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author cberm3o
 */
public interface IMovimientoService {

    MovimientoDTO createMotion(MovimientoDTO movimientos) throws Exception;

    MovimientoDTO updateMotion(String movimientoId, MovimientoDTO movimientos) throws Exception;

    void deleteMotion(String id) throws Exception;

    MovimientoDTO findById(String id) throws Exception;

    List<MovimientoDTO> findAll() throws Exception;
    List<MovimientoDTO> findByNumeroCuenta(String numerocuenta)throws Exception;

    double getTotalDebito(LocalDateTime starDate, LocalDateTime endDate, String codCuenta) throws Exception;
    double getTotalMovimientoByTipo(String codCuenta, String tipoMov)throws Exception;

    List<Object> findByDateAndUser(LocalDateTime startDate, LocalDateTime endDate, String usuario) throws Exception;

    byte[] generarReporte(LocalDateTime startDate, LocalDateTime endDate, String usuario) throws Exception;

}

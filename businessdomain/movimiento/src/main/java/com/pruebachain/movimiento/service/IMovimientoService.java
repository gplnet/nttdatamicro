/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

package com.pruebachain.movimiento.service;

import com.pruebachain.movimiento.entities.Movimiento;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author cberm3o
 */
public interface IMovimientoService {

  Movimiento registrar(Movimiento movimientos)throws Exception ;
	int modificar(Movimiento movimientos)throws Exception ;
	void eliminar(Long id)throws Exception ;
	Movimiento listarId(int id)throws Exception ;
	List<Movimiento> listar()throws Exception ;
	
	
	double getTotalDebito(LocalDateTime starDate, LocalDateTime endDate, String codCuenta )throws Exception ;
	List<Object> findByDateAndUser(LocalDateTime startDate, LocalDateTime endDate, String usuario)throws Exception ;
	byte[] generarReporte(LocalDateTime startDate, LocalDateTime endDate, String usuario) throws Exception ;

}

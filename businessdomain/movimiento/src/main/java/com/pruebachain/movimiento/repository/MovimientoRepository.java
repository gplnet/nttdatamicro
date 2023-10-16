/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Repository.java to edit this template
 */
package com.pruebachain.movimiento.repository;

import com.pruebachain.movimiento.entities.Movimiento;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

/**
 * @author cberm3o
 */
@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    @Query("SELECT t FROM Movimiento t WHERE t.numerocuenta_movimiento = ?1")
    public List<Movimiento> findByNumeroCuenta(String numerocuenta);
    
    @Query("SELECT t FROM Movimiento t WHERE t.movimiento_id = ?1")
    public Movimiento getMovimientoByMovId(String id);

    @Query(value = "select COALESCE(sum(valor_movimiento),0) as total from movimiento where fecha between ?1 and ?2 and tipo_movimiento='DEBITO'and numerocuenta_movimiento = ?3", nativeQuery = true)
    public double getTotalDebito(LocalDateTime starDate, LocalDateTime endDate, String codCuenta);

    @Query(value = "select movimientos.fecha, persona.nombre, cuenta.numero_cuenta, cuenta.tipo_cuenta, cuenta.saldo_inicial, cuenta.estado_cuenta, movimientos.valor_movimiento, movimientos.saldo_movimiento from movimientos inner join cuenta on cuenta.cuenta_id = movimientos.cuenta_id inner join cliente on cliente.cliente_id = cuenta.cliente_id inner join persona on persona.cliente_id = cliente.cliente_id where persona.nombre like %?3% and movimientos.fecha  between ?1 and ?2 order by movimientos.fecha asc", nativeQuery = true)
    public List<Object> findByDateAndUser(LocalDateTime startDate, LocalDateTime endDate, String usuario);

}

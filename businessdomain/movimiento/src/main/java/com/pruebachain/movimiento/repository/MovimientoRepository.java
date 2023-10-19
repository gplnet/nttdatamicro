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
    
    @Query(value = "select COALESCE(sum(valor_movimiento),0) as total from movimiento where tipo_movimiento=?1 and numerocuenta_movimiento = ?2", nativeQuery = true)
    public double getTotalMovimientoByTipo(String codCuenta, String tipoMov);

    @Query(value = "select * from movimiento t where t.numerocuenta_movimiento = ?3 and t.fecha  between ?1 and ?2 order by t.fecha asc", nativeQuery = true)
    public List<Object> findByDateAndUser(LocalDateTime startDate, LocalDateTime endDate, String usuario);

}

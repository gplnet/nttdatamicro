/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.pruebachain.movimiento.service.impl;

import com.pruebachain.movimiento.entities.Movimiento;
import com.pruebachain.movimiento.repository.MovimientoRepository;
import com.pruebachain.movimiento.service.IMovimientoService;
import com.pruebachain.movimiento.util.MovimientoReporte;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;




/**
 * @author cberm3o
 */
@Service
public class MovimientoServiceImpl implements IMovimientoService {

  @Autowired private MovimientoRepository dao;

  @Override
  public Movimiento registrar(Movimiento movimientos) throws Exception {
    return dao.save(movimientos);
  }

  @Override
  public int modificar(Movimiento movimientos) throws Exception {
    int rpst = (int) (dao.save(movimientos) != null ? movimientos.getMovimiento_id() : 0);
		return rpst > 0 ? 1 : 0;
  }

  @Override
  public void eliminar(Long id) throws Exception {
    dao.deleteById(id);
  }

  @Override
  public Movimiento listarId(int id) throws Exception {
    return dao.findById(Long.valueOf(id)).get();
  }

  @Override
  public List<Movimiento> listar() throws Exception {
    return dao.findAll();
  }

  @Override
  public double getTotalDebito(LocalDateTime starDate, LocalDateTime endDate, String codCuenta) throws Exception {
    return dao.getTotalDebito(starDate, endDate, codCuenta);
  }

  @Override
  public List<Object> findByDateAndUser(LocalDateTime startDate, LocalDateTime endDate, String usuario) throws Exception {
    return dao.findByDateAndUser(startDate, endDate, usuario);
  }

  @Override
  public byte[] generarReporte(LocalDateTime startDate, LocalDateTime endDate, String usuario) throws Exception {
    List<Object> listaMov = (List<Object>) dao.findByDateAndUser(startDate, endDate, usuario);
    List<MovimientoReporte> listaReporte = new ArrayList<>();

    Iterator itr = listaMov.iterator();
    while (itr.hasNext()) {
      Object[] obj = (Object[]) itr.next();

      MovimientoReporte mvr = new MovimientoReporte();
      mvr.setFecha(String.valueOf(obj[0]));
      mvr.setCliente(String.valueOf(obj[1]));
      mvr.setNumero_cuenta(String.valueOf(obj[2]));
      mvr.setTipo(String.valueOf(obj[3]));
      mvr.setSaldo_inicial(((BigDecimal) obj[4]).doubleValue());
      mvr.setEstado((String.valueOf(obj[5]).equals("ACTIVO") ? true : false));
      mvr.setMovimiento(String.valueOf(obj[6]));
      mvr.setSaldo_disponible(((BigDecimal) obj[7]).doubleValue());
      listaReporte.add(mvr);
    }

    JRBeanArrayDataSource ItemDataSource = new JRBeanArrayDataSource(listaReporte.toArray());
    Map<String, Object> cabecera = new HashMap<>();
    cabecera.put("ItemDataSource", ItemDataSource);

    File file = new ClassPathResource("/reportes/pruebaNtt.jasper").getFile();
    JasperPrint print =
        JasperFillManager.fillReport(file.getPath(), cabecera, new JREmptyDataSource());

    return JasperExportManager.exportReportToPdf(print);
  }
}

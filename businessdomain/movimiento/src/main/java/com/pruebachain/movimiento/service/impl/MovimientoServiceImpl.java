/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pruebachain.movimiento.service.impl;

import com.pruebachain.movimiento.dto.MovimientoDTO;
import com.pruebachain.movimiento.entities.Movimiento;
import com.pruebachain.movimiento.entities.response.ErrorMessages;
import com.pruebachain.movimiento.repository.MovimientoRepository;
import com.pruebachain.movimiento.service.IMovimientoService;
import com.pruebachain.movimiento.util.MovimientoReporte;
import com.pruebachain.movimiento.util.Utils;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

/**
 * @author cberm3o
 */
@Service
public class MovimientoServiceImpl implements IMovimientoService {
    
    @Autowired
    private MovimientoRepository dao;
    
    @Autowired
    private Utils utils;
    
    @Override
    public MovimientoDTO createMotion(MovimientoDTO movimientos) throws Exception {
        movimientos.setMovimiento_id(utils.generateMovId(30));
        Movimiento mov = new Movimiento();
        BeanUtils.copyProperties(movimientos, mov);
        
        Movimiento storedMov = dao.save(mov);
        MovimientoDTO returnValue = new MovimientoDTO();
        BeanUtils.copyProperties(storedMov, returnValue);
        return returnValue;
    }
    
    @Override
    public MovimientoDTO updateMotion(String movimientoId, MovimientoDTO movimientos) throws Exception {
        MovimientoDTO returnValue = new MovimientoDTO();
        Movimiento storedAcount = dao.getMovimientoByMovId(movimientoId);
        
        storedAcount.setTipo_movimiento(movimientos.getTipo_movimiento());
        storedAcount.setFecha(movimientos.getFecha());
        storedAcount.setTipo_movimiento(movimientos.getTipo_movimiento());
        storedAcount.setValor_movimiento(movimientos.getValor_movimiento());
        storedAcount.setSaldo_movimiento(movimientos.getSaldo_movimiento());
        storedAcount.setNumerocuenta_movimiento(movimientos.getNumerocuenta_movimiento());
        
        Movimiento updateMov = dao.save(storedAcount);
        BeanUtils.copyProperties(updateMov, returnValue);
        
        return returnValue;
    }
    
    @Override
    public void deleteMotion(String id) throws Exception {
        Movimiento storedMov = dao.getMovimientoByMovId(id);
        if (storedMov == null) {
            throw new Exception(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        dao.deleteById(storedMov.getId());
        
    }
    
    @Override
    public MovimientoDTO findById(String id) throws Exception {
        MovimientoDTO returnValue = new MovimientoDTO();
        Movimiento storedMov = dao.getMovimientoByMovId(id);
        if (storedMov == null) {
            throw new Exception(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }
        BeanUtils.copyProperties(storedMov, returnValue);
        return returnValue;
    }
    
     @Override
    public List<MovimientoDTO> findByNumeroCuenta(String numerocuenta) throws Exception {
        List<MovimientoDTO> returnValueList = new ArrayList<>();
        List<Movimiento> storedMovList = dao.findByNumeroCuenta(numerocuenta);
        for(Movimiento mov : storedMovList){
            MovimientoDTO returnValue = new MovimientoDTO();
            BeanUtils.copyProperties(mov, returnValue);
            returnValueList.add(returnValue);
        }
        return returnValueList;
    }
    
    @Override
    public List<MovimientoDTO> findAll() throws Exception {
        List<MovimientoDTO> returnValueList = new ArrayList<>();
        List<Movimiento> listaModel = dao.findAll();
        for (Movimiento movimiento : listaModel) {
            MovimientoDTO returnValue = new MovimientoDTO();
            BeanUtils.copyProperties(movimiento, returnValue);
            returnValueList.add(returnValue);
        }
        return returnValueList;
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
        JasperPrint print
                = JasperFillManager.fillReport(file.getPath(), cabecera, new JREmptyDataSource());
        
        return JasperExportManager.exportReportToPdf(print);
    }

    @Override
    public double getTotalMovimientoByTipo(String codCuenta, String tipoMov) throws Exception {
        return dao.getTotalMovimientoByTipo(codCuenta, tipoMov); 
    }

   
}

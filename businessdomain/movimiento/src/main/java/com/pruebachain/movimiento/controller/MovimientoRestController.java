/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */

package com.pruebachain.movimiento.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.pruebachain.movimiento.repository.MovimientoRepository;
import com.pruebachain.movimiento.entities.Movimiento;
import com.pruebachain.movimiento.exception.MovimientosServiceExceptions;
import com.pruebachain.movimiento.util.ErrorMessages;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * @author cberm3o
 */
@RestController
@RequestMapping("/movimientos")
public class MovimientoRestController {
  
  public static final Double LIMITE = 100.00;
  public static final Double BASE = 0.00;
  
  
  public static final String DEBITO = "DEBITO";
  

  @Autowired 
  private MovimientoRepository movimientoRepository;
  
  
   private final WebClient.Builder webClientBuilder;
  
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  public MovimientoRestController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
  }
  HttpClient client = HttpClient.create()
            //Connection Timeout: is a period within which a connection between a client and a server must be established
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            //Response Timeout: The maximun time we wait to receive a response after sending a request
            .responseTimeout(Duration.ofSeconds(1))
            // Read and Write Timeout: A read timeout occurs when no data was read within a certain 
            //period of time, while the write timeout when a write operation cannot finish at a specific time
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

  @GetMapping( value = "/listar")
  public ResponseEntity<List<Movimiento>> findAll() {
     List<Movimiento> mov = new ArrayList<>();
    try {
      mov = (List<Movimiento>) movimientoRepository.findAll();
    } catch (Exception e) {
      new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<Movimiento>>((List<Movimiento>) mov, HttpStatus.OK);
  }
  
  
  @GetMapping(value = "/cuenta/all")
  public ResponseEntity<List<Movimiento>> listarByCuenta(@RequestParam String cuenta) {
    logger.info("listarByCuenta"+cuenta);
    List<Movimiento> mov = new ArrayList<>();
    try {
      mov = movimientoRepository.findByNumeroCuenta(cuenta);
      logger.info("listarByCuenta"+mov.toString());
    } catch (Exception e) {
       return new ResponseEntity<List<Movimiento>>(mov, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<Movimiento>>(mov, HttpStatus.OK);
  }

  @GetMapping(value = "/listar/{id}")
  public ResponseEntity<Movimiento> listarId(@PathVariable Long id) {
    Movimiento mov = new Movimiento();
    try {
      mov = movimientoRepository.findById(id).get();
    } catch (Exception e) {
       return new ResponseEntity<Movimiento>(mov, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Movimiento>(mov, HttpStatus.OK);
  }
  
  @GetMapping( value = "/listByDateAndUser/{starDate}/{endDate}/{usuario}")
	public ResponseEntity<List<Object>> listByDateAndUser(@PathVariable("starDate") String starDate, @PathVariable("endDate") String endDate, @PathVariable("usuario") String usuario) throws Exception{
		List<Object> listado = new ArrayList<>();		
		try {
			
			
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			
			
			LocalDate date = LocalDate.parse(starDate);
			LocalDate dateEnd = LocalDate.parse(endDate);
			
			LocalDateTime beginDate = LocalDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00", Locale.ENGLISH).format(date), formatter);
			LocalDateTime afterDate = LocalDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59", Locale.ENGLISH).format(dateEnd), formatter);
			
						
			
			
			listado = movimientoRepository.findByDateAndUser(beginDate, afterDate, usuario);
			
			
		} catch (Exception e) {
			// TODO: handle exception			
			new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<Object>>(listado, HttpStatus.OK);
	}

  @PutMapping(value = "/actualizar/{id}")
  public ResponseEntity<Integer> put(@RequestBody Movimiento input) {
   int rpsta = 0;
    try {
       rpsta = (int) (movimientoRepository.save(input)!= null ? input.getMovimiento_id(): 0);
       //rpsta > 0 ? 0 : 1;
    } catch (Exception e) {
      return new ResponseEntity<Integer>(rpsta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
       return new ResponseEntity<Integer>(rpsta, HttpStatus.OK);
  }
  @PostMapping(value = "/registrar/{cuenta}")
	public ResponseEntity<Object> registrar(@RequestBody Movimiento mov , @PathVariable("cuenta") String cuenta ) throws MovimientosServiceExceptions{		
		
		
		LocalDateTime ldt = LocalDateTime.now();
				
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		try {	
      //JsonNode jsp = getCuentaName(cc.getCuenta_id()); jsp.get("cuenta_id").asText()
      JsonNode jsp = getCuentaByNumber(cuenta);
			//Cuenta getCuenta = cuentaService.getCuentaByNumber(cuenta);	
			logger.info("jsp"+jsp.get("cuenta_id").asLong());
			if(jsp.get("cuenta_id").asLong() > 0) {
				//mov.setCuenta(getCuenta);
        logger.info("jsp"+jsp.toString());
        String numc = jsp.get("numero_cuenta").asText();
        logger.info("jsp"+jsp.get("numero_cuenta").asText());
				double getLimite = movimientoRepository.getTotalDebito(LocalDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00", Locale.ENGLISH).format(ldt), formatter), LocalDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59", Locale.ENGLISH).format(ldt), formatter), numc);
				logger.info("jsp"+getLimite);
				Double saldoAct = jsp.get("saldo_inicial").asDouble();//getCuenta.getSaldoInicial();			
				logger.info("jsp"+saldoAct);
				if(saldoAct > mov.getValor_movimiento()) {
					if(getLimite < LIMITE && (getLimite+mov.getValor_movimiento()<= LIMITE)) {
						Movimiento movimiento = new Movimiento();
						mov.setSaldo_movimiento(saldoAct - mov.getValor_movimiento());
						mov.setFecha(ldt);
						//mov.getCuenta().setSaldoInicial(saldoAct - mov.getValorMovimeinto());						
						movimiento = movimientoRepository.save(mov);
            logger.info("jsp"+movimiento.toString());
					}else {				
						throw new Exception(ErrorMessages.DAILY_QUOTA_EXCEEDED.getErrorMessage());						
					}
				}else {
					throw new Exception(ErrorMessages.BALANCE_NOT_AVAILABLE.getErrorMessage());
				}		
			}
				
			
		} catch (Exception e) {
			// TODO: handle exception			
			return new ResponseEntity<>(e.getMessage(), new HttpHeaders(),HttpStatus.INTERNAL_SERVER_ERROR );
		}
		
		return new ResponseEntity<>(ErrorMessages.CREATED_OK.getErrorMessage(), HttpStatus.OK);
		
	}

  @PostMapping(value = "/registrar")
  public ResponseEntity<Movimiento> post(@RequestBody Movimiento input) {
     Movimiento mov = new Movimiento();
    try {
      mov = movimientoRepository.save(input);
    } catch (Exception e) {
      return new ResponseEntity<Movimiento>(mov, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Movimiento>(mov, HttpStatus.OK);
  }

 @DeleteMapping(value = "/eliminar/{id}")
  public ResponseEntity<Integer> delete(@PathVariable Long id) {
    int resultado = 0;
    try {
      movimientoRepository.deleteById(id);
      resultado = 1;
    } catch (Exception e) {
      resultado = 0;
      return new ResponseEntity<Integer>(resultado, HttpStatus.INTERNAL_SERVER_ERROR);
      
    }
    return new ResponseEntity<Integer>(resultado, HttpStatus.OK);
  }
  
  
  
  private JsonNode getCuentaByNumber(String numero) { 
    logger.info("getCuentaByNumber"+numero);
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8832/cuentas")//bussinesdomain-cuentas
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8832/cuentas"))//bussinesdomain-cuentas
                .build();
        JsonNode block = build.method(HttpMethod.GET).uri("/cuentaListar/" + numero)
                .retrieve().bodyToMono(JsonNode.class).block();
        logger.info("->"+block.toString());
        //String name = block.get("tipo_cuenta").asText();
        //logger.info("->"+name);
        return block;
    }
}

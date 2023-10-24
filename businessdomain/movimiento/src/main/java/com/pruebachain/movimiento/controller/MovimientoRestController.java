/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.pruebachain.movimiento.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.pruebachain.movimiento.dto.MovimientoDTO;
import com.pruebachain.movimiento.repository.MovimientoRepository;
import com.pruebachain.movimiento.entities.Movimiento;
import com.pruebachain.movimiento.entities.request.MovimientoRequestModel;
import com.pruebachain.movimiento.entities.response.MovimientoRest;
import com.pruebachain.movimiento.entities.response.OperationStatusModel;
import com.pruebachain.movimiento.entities.response.OperationsName;
import com.pruebachain.movimiento.entities.response.RequestOperationStatus;
import com.pruebachain.movimiento.exception.MovimientosServiceExceptions;
import com.pruebachain.movimiento.service.IMovimientoService;
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
import java.util.Arrays;
import java.util.Collections;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.modelmapper.ModelMapper;
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
     public static final String CREDITO = "CREDITO";

    @Autowired
    private IMovimientoService movimientoRepository;

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

    @GetMapping(value = "/listar")
    public ResponseEntity<List<MovimientoRest>> findAll() {
        List<MovimientoRest> movList = new ArrayList<>();
        try {
            List<MovimientoDTO> listDTO = movimientoRepository.findAll();
            for (MovimientoDTO movimientoDTO : listDTO) {
                ModelMapper modelMapper = new ModelMapper();
                MovimientoRest movimientoRest = modelMapper.map(movimientoDTO, MovimientoRest.class);
                movList.add(movimientoRest);
            }
        } catch (Exception e) {
            new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<MovimientoRest>>((List<MovimientoRest>) movList, HttpStatus.OK);
    }

    @GetMapping(value = "/cuenta/all")
    public ResponseEntity<List<MovimientoRest>> listarByCuenta(@RequestParam String cuenta) {

        List<MovimientoRest> mov = new ArrayList<>();
        try {
            List<MovimientoDTO> listaDTO = movimientoRepository.findByNumeroCuenta(cuenta);
            for (MovimientoDTO movi : listaDTO) {
                ModelMapper modelMapper = new ModelMapper();
                MovimientoRest movRest = modelMapper.map(movi, MovimientoRest.class);
                mov.add(movRest);
            }

        } catch (Exception e) {
            return new ResponseEntity<List<MovimientoRest>>(mov, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<MovimientoRest>>(mov, HttpStatus.OK);
    }

    @GetMapping(value = "/listar/{id}")
    public ResponseEntity<MovimientoRest> listarId(@PathVariable String id) {
        MovimientoRest mov = new MovimientoRest();
        try {
            ModelMapper modelMapper = new ModelMapper();
            MovimientoDTO searchMov = movimientoRepository.findById(id);

            mov = modelMapper.map(searchMov, MovimientoRest.class);
        } catch (Exception e) {
            return new ResponseEntity<MovimientoRest>(mov, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<MovimientoRest>(mov, HttpStatus.OK);
    }

    @GetMapping(value = "/listByDateAndUser/{starDate}/{endDate}/{usuario}")
    public ResponseEntity<List<Object>> listByDateAndUser(@PathVariable("starDate") String starDate, @PathVariable("endDate") String endDate, @PathVariable("usuario") String usuario) throws Exception {
        List<Object> listado = new ArrayList<>();
        try {
            
            JsonNode jspClient = getClienteByName(usuario);
            Long idCliente = jspClient.get("id").asLong();
            logger.info("findAll-2" + idCliente.toString());
            JsonNode jspCCuenta = getCuentaById(idCliente);
            //numero_cuenta
            logger.info("findAll-2" + jspCCuenta.toString());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            LocalDate date = LocalDate.parse(starDate);
            LocalDate dateEnd = LocalDate.parse(endDate);

            LocalDateTime beginDate = LocalDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00", Locale.ENGLISH).format(date), formatter);
            LocalDateTime afterDate = LocalDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59", Locale.ENGLISH).format(dateEnd), formatter);
            logger.info("fecha" + beginDate.toString());
            logger.info("fecha2: " + afterDate.toString());
            logger.info("fecha2: " + jspCCuenta.get("numero_cuenta").asText());
            listado = movimientoRepository.findByDateAndUser(beginDate, afterDate, jspCCuenta.get("numero_cuenta").asText());
            logger.info("fecha2: " + listado.size());
            listado.forEach(x -> {
                logger.info("fecha2: " + x.toString());
                
            });
        } catch (Exception e) {
            // TODO: handle exception			
            new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<Object>>(listado, HttpStatus.OK);
    }

    @PutMapping(value = "/actualizar/{id}")
    public ResponseEntity<OperationStatusModel> put(@PathVariable String id, @RequestBody MovimientoRequestModel input) {
        OperationStatusModel returnValue = new OperationStatusModel();
        try {
            ModelMapper modelMapper = new ModelMapper();
            MovimientoDTO movDTO = modelMapper.map(input, MovimientoDTO.class);
            returnValue.setOpretationName(OperationsName.UPDATE.name());
            MovimientoDTO createdMov = movimientoRepository.updateMotion(id, movDTO);
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
            //rpsta > 0 ? 0 : 1;
        } catch (Exception e) {
            return new ResponseEntity<OperationStatusModel>(returnValue, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<OperationStatusModel>(returnValue, HttpStatus.OK);
    }

    @PostMapping(value = "/registrar")
    public ResponseEntity<Object> registrar(@RequestBody MovimientoRequestModel mov) throws MovimientosServiceExceptions {
        MovimientoRest movim = new MovimientoRest();
        LocalDateTime ldt = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
 
        try {
            //JsonNode jsp = getCuentaName(cc.getCuenta_id()); jsp.get("cuenta_id").asText()
            JsonNode jsp = getCuentaByNumber(mov.getNumerocuenta_movimiento());
            //Cuenta getCuenta = cuentaService.getCuentaByNumber(cuenta);	
            logger.info("jsp" + jsp.get("cuenta_id").asLong());
            if (jsp.get("cuenta_id") != null) {
                //mov.setCuenta(getCuenta);
                logger.info("jsp" + jsp.toString());
                String numc = jsp.get("numero_cuenta").asText();
                logger.info("jsp" + jsp.get("numero_cuenta").asText());
                double getLimite = movimientoRepository.getTotalDebito(LocalDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00", Locale.ENGLISH).format(ldt), formatter), LocalDateTime.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59", Locale.ENGLISH).format(ldt), formatter), numc);
                double getLimiteTotalDeb = movimientoRepository.getTotalMovimientoByTipo(DEBITO, numc);
                double getLimiteTotalCred = movimientoRepository.getTotalMovimientoByTipo(CREDITO, numc);
                logger.info("jsp" + getLimite);
                Double saldoAct = jsp.get("saldo_inicial").asDouble();//getCuenta.getSaldoInicial();			
                logger.info("jsp" + saldoAct);
                if (saldoAct > mov.getValor_movimiento()) {
                    if (getLimite < LIMITE && (getLimite + mov.getValor_movimiento() <= LIMITE)) {
                        
                        ModelMapper modelMapper = new ModelMapper();
                        MovimientoDTO createdMovDTO = modelMapper.map(mov, MovimientoDTO.class);
                        if(createdMovDTO.getTipo_movimiento().equals(DEBITO)){                            
                            createdMovDTO.setSaldo_movimiento(((saldoAct-getLimiteTotalDeb)+getLimiteTotalCred) - mov.getValor_movimiento());
                        }else{                            
                            createdMovDTO.setSaldo_movimiento(((saldoAct+getLimiteTotalCred)-getLimiteTotalDeb) + mov.getValor_movimiento());
                        }
                        
                        mov.setFecha(ldt);
                        //mov.getCuenta().setSaldoInicial(saldoAct - mov.getValorMovimeinto());
                        
                        MovimientoDTO movDTO = movimientoRepository.createMotion(createdMovDTO);
                        movim = modelMapper.map(movDTO, MovimientoRest.class);
                        logger.info("jsp" + movim.toString());
                    } else {
                        throw new Exception(ErrorMessages.DAILY_QUOTA_EXCEEDED.getErrorMessage());
                    }
                } else {
                    throw new Exception(ErrorMessages.BALANCE_NOT_AVAILABLE.getErrorMessage());
                }
            }else{
                throw new Exception(ErrorMessages.ACCOUNT_DOES_EXITS.getErrorMessage());
            }

        } catch (Exception e) {
            // TODO: handle exception			
            return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(ErrorMessages.CREATED_OK.getErrorMessage(), HttpStatus.OK);

    }

    /*@PostMapping(value = "/registrar")
    public ResponseEntity<MovimientoRest> post(@RequestBody MovimientoRequestModel input) {
        MovimientoRest mov = new MovimientoRest();
        try {

            ModelMapper modelMapper = new ModelMapper();
            MovimientoDTO createdMovDTO = modelMapper.map(input, MovimientoDTO.class);
            MovimientoDTO movDTO = movimientoRepository.createMotion(createdMovDTO);
            mov = modelMapper.map(movDTO, MovimientoRest.class);

        } catch (Exception e) {
            return new ResponseEntity<MovimientoRest>(mov, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<MovimientoRest>(mov, HttpStatus.OK);
    }*/

    @DeleteMapping(value = "/eliminar/{id}")
    public ResponseEntity<OperationStatusModel> delete(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        try {
            returnValue.setOpretationName(OperationsName.DELETE.name());
            movimientoRepository.deleteMotion(id);
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        } catch (Exception e) {
            return new ResponseEntity<OperationStatusModel>(returnValue, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<OperationStatusModel>(returnValue, HttpStatus.OK);
    }
    
    private JsonNode getCuentaById(Long id) {
        logger.info("getCuentaByNumber" + id);
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8832/cuentas")//bussinesdomain-cuentas
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8832/cuentas"))//bussinesdomain-cuentas
                .build();
        JsonNode block = build.method(HttpMethod.GET).uri("/search/" + id)
                .retrieve().bodyToMono(JsonNode.class).block();
        logger.info("->" + block.toString());
        //String name = block.get("tipo_cuenta").asText();
        //logger.info("->"+name);
        return block;
    }

    private JsonNode getCuentaByNumber(String numero) {
        logger.info("getCuentaByNumber" + numero);
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8832/cuentas")//bussinesdomain-cuentas
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8832/cuentas"))//bussinesdomain-cuentas
                .build();
        JsonNode block = build.method(HttpMethod.GET).uri("/cuentaListar/" + numero)
                .retrieve().bodyToMono(JsonNode.class).block();
        logger.info("->" + block.toString());
        //String name = block.get("tipo_cuenta").asText();
        //logger.info("->"+name);
        return block;
    }
    
    private JsonNode getClienteByName(String name) {
        logger.info("getCuentaByNumber" + name);
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8899/clientes")//bussinesdomain-cuentas
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8832/cuentas"))//bussinesdomain-cuentas
                .build();
        JsonNode block = build.method(HttpMethod.GET).uri("/listarClienteByName/" + name)
                .retrieve().bodyToMono(JsonNode.class).block();
        logger.info("->" + block.toString());
        //String name = block.get("tipo_cuenta").asText();
        //logger.info("->"+name);
        return block;
    }
}

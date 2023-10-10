/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */

package com.pruebachain.cliente.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.pruebachain.cliente.repository.ClienteRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;



import com.pruebachain.cliente.entities.Cliente;

import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * @author cberm3o
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {

  @Autowired 
  private ClienteRepository clienteRepository;
  
  private final WebClient.Builder webClientBuilder;
  
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  public ClienteController(WebClient.Builder webClientBuilder) {
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
  public ResponseEntity<List<Cliente>> findAll() {
    List<Cliente> client = new ArrayList<>();   
    try {
      client = (List<Cliente>) clienteRepository.findAll();
      client.forEach(
          x -> {            
            x.getCuentas()
                .forEach(
                    y -> {
                      
                      JsonNode jsp = getCuentaName(y.getCuenta_id());
                      logger.info("findAll"+jsp.toString());
                      String tipoCuenta = jsp.get("tipo_cuenta").asText();
                      y.setCuenta_tipo(tipoCuenta);                      
                      
                      List<?> mov = getTransactions(jsp.get("numero_cuenta").asText());
                      logger.info("findAll"+mov.toString());
                      x.setMovientos(mov);
                      //mov = movimientos;
                    });
            logger.info("x"+x.getMovientos().toString());
            //x.setMovientos(mov);
          });

      

    } catch (Exception e) {
      new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<List<Cliente>>((List<Cliente>) client, HttpStatus.OK);
  }
  
  
  

  @GetMapping(value = "/listar/{id}")
  public ResponseEntity<Cliente> listarId(@PathVariable Long id) {
    Cliente client = new Cliente();
    try {
      client = clienteRepository.findById(id).get();
    } catch (Exception e) {
      return new ResponseEntity<Cliente>(client, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Cliente>(client, HttpStatus.OK);	
  }

  @PutMapping(value = "/actualizar/{id}")
  public ResponseEntity<Integer> put(@PathVariable long id, @RequestBody Cliente input) {  
     
    int rpsta = 0;
    try {
      Cliente find = clienteRepository.findById(id).get();
      if(find != null){
        find.setCliente_id(input.getCliente_id());

      }
       rpsta = (int) (clienteRepository.save(input)!= null ? input.getCliente_id(): 0);
       //rpsta > 0 ? 0 : 1;
    } catch (Exception e) {
      return new ResponseEntity<Integer>(rpsta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
       return new ResponseEntity<Integer>(rpsta, HttpStatus.OK);
  }

  @PostMapping(value = "/registrar")
  public ResponseEntity<Cliente> post(@RequestBody Cliente input) {
    Cliente client = new Cliente();
    try {
      input.getCuentas().forEach(x -> x.setCliente(input));
      client = clienteRepository.save(input);
    } catch (Exception e) {
      return new ResponseEntity<Cliente>(client, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    return new ResponseEntity<Cliente>(client, HttpStatus.OK);
  }
  
 

  @DeleteMapping(value = "/eliminar/{id}")
  public  ResponseEntity<Integer> delete(@PathVariable Long id) {
    int resultado = 0;
    try {
      clienteRepository.deleteById(id);
      resultado = 1;
    } catch (Exception e) {
      resultado = 0;
      return new ResponseEntity<Integer>(resultado, HttpStatus.INTERNAL_SERVER_ERROR);
      
    }
    return new ResponseEntity<Integer>(resultado, HttpStatus.OK);
  }
  
  private JsonNode getCuentaName(long id) { 
    logger.info("getCuentaName"+id);
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8832/cuentas")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8832/cuentas"))
                .build();
        JsonNode block = build.method(HttpMethod.GET).uri("/listar/" + id)
                .retrieve().bodyToMono(JsonNode.class).block();
        logger.info("->"+block.toString());
        //String name = block.get("tipo_cuenta").asText();
        //logger.info("->"+name);
        return block;
    }
  
  
  private  List<?> getTransactions(String  numero) { 
    logger.info("getTransactions"+numero);
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8833/movimientos")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)               
                .build();
        
        
         List<?> transactions = build.method(HttpMethod.GET).uri(uriBuilder -> uriBuilder
                .path("/cuenta/all")
                .queryParam("cuenta", numero)               
                .build())
                .retrieve().bodyToFlux(Object.class).collectList().block();

      logger.info("getTransactions"+transactions);
        return transactions;
    }
}

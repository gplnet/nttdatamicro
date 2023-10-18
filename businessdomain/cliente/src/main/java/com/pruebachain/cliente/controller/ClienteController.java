/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/RestController.java to edit this template
 */
package com.pruebachain.cliente.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.pruebachain.cliente.dto.ClienteDTO;
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
import com.pruebachain.cliente.entities.ClienteCuenta;
import com.pruebachain.cliente.entities.request.ClientRequestModel;
import com.pruebachain.cliente.entities.response.ClientRest;
import com.pruebachain.cliente.entities.response.OperationStatusModel;
import com.pruebachain.cliente.entities.response.OperationsName;
import com.pruebachain.cliente.entities.response.RequestOperationStatus;
import com.pruebachain.cliente.service.IClienteService;

import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.modelmapper.ModelMapper;
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
    private IClienteService clienteRepository;

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

    @GetMapping(value = "/listar")
    public ResponseEntity<List<ClientRest>> findAll() {
        List<ClientRest> client = new ArrayList<>();
        Map<String, Object> lista = new HashMap<>();
        Map<String, List<?>> movimientos = new HashMap<>();
        try {
            List<ClienteDTO> listDTO = clienteRepository.listar();
            for (ClienteDTO clientDTO : listDTO) {
                ModelMapper modelMapper = new ModelMapper();
                ClientRest clientRest = modelMapper.map(clientDTO, ClientRest.class);
                client.add(clientRest);
            }
            //client = (List<Cliente>) clienteRepository.listar();//client Cliente
            client.forEach(
                    c -> {
                        c.getCuentas()
                                .forEach(
                                        cc -> {
                                            JsonNode jsp = getCuentaById(cc.getCuenta_id());
                                            cc.setCuenta_tipo(jsp.get("tipo_cuenta").asText());
                                            //c.setMovientos(getTransactions(jsp.get("numero_cuenta").asText()));
                                            lista.put("" + cc.getCuenta_id(), jsp.get("numero_cuenta").asText());
                                        });
                    });
            logger.info("v+" + lista.toString());

            lista.entrySet().stream()
                    .forEach(
                            m -> {
                                movimientos.put(m.getKey(), getTransactions((String) m.getValue()));
                            });
            logger.info("m+" + movimientos.toString());
            for (ClientRest c : client) {
                if (!c.getCuentas().isEmpty()) {

                    for (ClienteCuenta cc : c.getCuentas()) {
                        movimientos.entrySet().stream()
                                .forEach(
                                        t -> {
                                            logger.info("t1 : " + t.getKey());
                                            logger.info("t2 : " + cc.getCuenta_id());
                                            if (Long.parseLong(t.getKey()) == cc.getCuenta_id()) {
                                                c.setMovientos(t.getValue());
                                                logger.info("t3 : " + c.getMovientos().toString());
                                            }
                                        });
                    }
                }
            }
        } catch (Exception e) {
            new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<List<ClientRest>>((List<ClientRest>) client, HttpStatus.OK);
    }

    @GetMapping(value = "/listar/{id}")
    public ResponseEntity<ClientRest> listarId(@PathVariable String id) {
        ClientRest client = new ClientRest();
        try {
            ModelMapper modelMapper = new ModelMapper();
            ClienteDTO clientDTO = clienteRepository.listarId(id);
            client = modelMapper.map(clientDTO, ClientRest.class);
        } catch (Exception e) {
            return new ResponseEntity<ClientRest>(client, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<ClientRest>(client, HttpStatus.OK);
    }

    @GetMapping(value = "/listarCliente/{cuenta}")
    public ResponseEntity<Object> listarClienteByAccount(@PathVariable String cuenta) {
        Object obj = new Object();
        try {

            JsonNode jsp = getCuentaByNumber(cuenta);//ID CUENTA
            logger.info("findAll-3" + jsp.toString());
            obj = clienteRepository.findClienteByCodeAccount(jsp.get("cuenta_id").asLong());
            logger.info("findAll-3" + obj.toString());
        } catch (Exception e) {
            return new ResponseEntity<Object>(obj, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(obj, HttpStatus.OK);
    }
    
     @GetMapping(value = "/listarClienteByName/{name}")
    public ResponseEntity<Object> listarClienteByName(@PathVariable String name) {
        Object obj = new Object();
        try {
            obj = clienteRepository.findClienteByName(name);
            logger.info("findAll-3" + obj.toString());
        } catch (Exception e) {
            return new ResponseEntity<Object>(obj, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(obj, HttpStatus.OK);
    }
    

    @PutMapping(value = "/actualizar/{id}")
    public ResponseEntity<OperationStatusModel> put(@PathVariable String id, @RequestBody ClientRequestModel input) {
        OperationStatusModel returnValue = new OperationStatusModel();
        try {
            ModelMapper modelMapper = new ModelMapper();
            ClienteDTO clienteDTO = modelMapper.map(input, ClienteDTO.class);
            returnValue.setOpretationName(OperationsName.UPDATE.name());
            ClienteDTO find = clienteRepository.modificar(id, clienteDTO);
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

            //rpsta > 0 ? 0 : 1;
        } catch (Exception e) {
            return new ResponseEntity<OperationStatusModel>(returnValue, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<OperationStatusModel>(returnValue, HttpStatus.OK);
    }

    @PostMapping(value = "/registrar")
    public ResponseEntity<ClientRest> post(@RequestBody ClientRest input) {
        ClientRest client = new ClientRest();
        try {
            ModelMapper modelMapper = new ModelMapper();
            //input.getCuentas().forEach(x -> x.setCliente(input));
            ClienteDTO clientDTO = modelMapper.map(input, ClienteDTO.class);
            //input.getCuentas().forEach((x) -> {x.setCliente(input);});

            //client = clienteRepository.registrar(input);
            ClienteDTO createdClient = clienteRepository.registrar(clientDTO);
            client = modelMapper.map(createdClient, ClientRest.class);

        } catch (Exception e) {
            return new ResponseEntity<ClientRest>(client, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ClientRest>(client, HttpStatus.OK);
    }

    @DeleteMapping(value = "/eliminar/{id}")
    public ResponseEntity<OperationStatusModel> delete(@PathVariable String id) {
        OperationStatusModel returnValue = new OperationStatusModel();
        try {
            returnValue.setOpretationName(OperationsName.DELETE.name());
            clienteRepository.eliminar(id);
            returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

        } catch (Exception e) {

            return new ResponseEntity<OperationStatusModel>(returnValue, HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<OperationStatusModel>(returnValue, HttpStatus.OK);
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
    
    private JsonNode getCuentaById(long id) {
        logger.info("getCuentaName" + id);
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

    private JsonNode getCuentaName(String id) {
        logger.info("getCuentaName" + id);
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8832/cuentas")//bussinesdomain-cuentas
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "http://localhost:8832/cuentas"))//bussinesdomain-cuentas
                .build();
        JsonNode block = build.method(HttpMethod.GET).uri("/listar/" + id)
                .retrieve().bodyToMono(JsonNode.class).block();
        logger.info("->" + block.toString());
        //String name = block.get("tipo_cuenta").asText();
        //logger.info("->"+name);
        return block;
    }

    private List<?> getTransactions(String numero) {
        logger.info("getTransactions" + numero);
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(client))
                .baseUrl("http://localhost:8833/movimientos")//bussinesdomain-movimientos
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        List<?> transactions = build.method(HttpMethod.GET).uri(uriBuilder -> uriBuilder
                .path("/cuenta/all")
                .queryParam("cuenta", numero)
                .build())
                .retrieve().bodyToFlux(Object.class).collectList().block();

        logger.info("getTransactions" + transactions);
        return transactions;
    }
}

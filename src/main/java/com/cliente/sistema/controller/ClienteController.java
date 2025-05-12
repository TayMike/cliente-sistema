package com.cliente.sistema.controller;

import com.cliente.sistema.entities.Cliente;
import com.cliente.sistema.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.String;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @GetMapping("/{cpf}")
    public ResponseEntity<Cliente> encontrarCliente(@PathVariable String cpf) {
        Optional<Cliente> cliente = clienteService.encontrarCliente(cpf);
        return cliente.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Cliente>> encontrarClientes() {
        List<Cliente> clientes = clienteService.encontrarClientes();
        if (clientes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(clientes);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Cliente> cadastrarClientes(@RequestBody Cliente cliente) {
        try {
            Cliente clienteCadastrado = clienteService.cadastrarCliente(cliente);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{cpf}")
                    .buildAndExpand(clienteCadastrado.getCpf())
                    .toUri();
            return ResponseEntity.created(uri).body(clienteCadastrado);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Cliente> alterarClientes(@RequestBody Cliente cliente) {
        Optional<Cliente> clienteVerificado = clienteService.encontrarCliente(cliente.getCpf());
        if (clienteVerificado.isPresent()) {
            Cliente clienteAlterado = clienteService.alterarCliente(cliente);
            return ResponseEntity.ok(clienteAlterado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Cliente> deletarClientes(@PathVariable String cpf) {
        Optional<Cliente> cliente = clienteService.encontrarCliente(cpf);
        if (cliente.isPresent()) {
            clienteService.deletarCliente(cpf);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}

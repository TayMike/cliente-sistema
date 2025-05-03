package com.cliente.sistema.controller;

import com.cliente.sistema.entities.Cliente;
import com.cliente.sistema.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.String;
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
            return ResponseEntity.ok(clienteCadastrado);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/alterar")
    public ResponseEntity<Cliente> alterarClientes(@RequestBody Cliente cliente) {
        try {
            Cliente clienteAlterado = clienteService.alterarCliente(cliente);
            return ResponseEntity.ok(clienteAlterado);
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/deletar/{cpf}")
    public ResponseEntity<Cliente> deletarClientes(@PathVariable String cpf) {
        try {
            clienteService.deletarCliente(cpf);
            return ResponseEntity.ok().build();
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}

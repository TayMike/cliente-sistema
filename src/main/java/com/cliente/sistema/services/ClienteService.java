package com.cliente.sistema.services;

import com.cliente.sistema.entities.Cliente;
import com.cliente.sistema.repositories.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    public Optional<Cliente> encontrarCliente(String cpf) {
        return clienteRepository.findById(cpf);
    }

    public List<Cliente> encontrarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente cadastrarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente alterarCliente(Cliente clienteNovo) {
        if(clienteRepository.existsById(clienteNovo.getCpf())) {
            Cliente clienteVelho = clienteRepository.getReferenceById(clienteNovo.getCpf());
            clienteVelho.setLogradouro(clienteNovo.getLogradouro());
            clienteVelho.setNumero(clienteNovo.getNumero());
            clienteVelho.setBairro(clienteNovo.getBairro());
            clienteVelho.setCidade(clienteNovo.getCidade());
            clienteVelho.setUf(clienteNovo.getUf());
            clienteVelho.setComplemento(clienteNovo.getComplemento());
            clienteVelho.setDataNascimento(clienteNovo.getDataNascimento());
            return clienteRepository.save(clienteVelho);
        }
        return null;
    }

    public void deletarCliente(String cliente) {
        clienteRepository.deleteById(cliente);
    }

}

package com.cliente.sistema.servicesTest;

import com.cliente.sistema.entities.Cliente;
import com.cliente.sistema.repositories.ClienteRepository;
import com.cliente.sistema.services.ClienteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private AutoCloseable openMocks;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);

        cliente = Cliente.builder()
                .cpf("11111111111")
                .dataNascimento(LocalDate.of(2000, 10, 10))
                .cep("11111111")
                .logradouro("Teste")
                .bairro("Teste")
                .cidade("Teste")
                .uf("TE")
                .numero(111)
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testEncontrarCliente() {
        // Arrange
        when(clienteRepository.findById(cliente.getCpf())).thenReturn(Optional.of(cliente));

        // Act
        Optional<Cliente> resultado = clienteService.encontrarCliente(cliente.getCpf());

        // Assert
        assertNotNull(resultado);
        assertEquals(cliente.getCpf(), resultado.get().getCpf());
        verify(clienteRepository, times(1)).findById(cliente.getCpf());
    }

    @Test
    void testEncontrarTodosCliente() {
        // Arrange
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        // Act
        List<Cliente> resultado = clienteService.encontrarClientes();

        // Assert
        assertNotNull(resultado);
        assertEquals(cliente.getCpf(), resultado.getFirst().getCpf());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testCadastrarCliente() {
        // Arrange
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.cadastrarCliente(cliente);

        // Assert
        assertNotNull(resultado);
        assertEquals(cliente.getCpf(), resultado.getCpf());
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testAtualizarCliente_Success() {
        // Arrange
        when(clienteRepository.existsById(cliente.getCpf())).thenReturn(true);
        when(clienteRepository.getReferenceById(cliente.getCpf())).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        Cliente resultado = clienteService.alterarCliente(cliente);

        // Assert
        assertNotNull(resultado);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testAtualizarCliente_Null() {
        // Arrange
        when(clienteRepository.existsById(cliente.getCpf())).thenReturn(false);

        // Act
        Cliente resultado = clienteService.alterarCliente(cliente);

        // Assert
        assertNull(resultado);
        verify(clienteRepository, times(0)).save(cliente);
    }

    @Test
    void testDeletarCliente() {
        // Arrange & Act
        clienteService.deletarCliente(cliente.getCpf());

        // Assert
        verify(clienteRepository, times(1)).deleteById(cliente.getCpf());
    }

}

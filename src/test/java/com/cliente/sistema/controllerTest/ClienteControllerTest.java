package com.cliente.sistema.controllerTest;

import com.cliente.sistema.controller.ClienteController;
import com.cliente.sistema.entities.Cliente;
import com.cliente.sistema.services.ClienteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ClienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private String cpf = "11111111111";
    private Cliente cliente;
    private AutoCloseable openMocks;
    private String json = """
                {
                    "cpf": "11111111111",
                    "dataNascimento": "2000-10-10",
                    "cep": "11111111",
                    "logradouro": "Teste",
                    "bairro": "Teste",
                    "cidade": "Teste",
                    "uf": "TE",
                    "numero": 111
                }
                """;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();

        cliente = Cliente.builder()
                .cpf(cpf)
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
    void testEncontrarCliente_Success() throws Exception {
        // Arrange
        when(clienteService.encontrarCliente(cliente.getCpf())).thenReturn(Optional.of(cliente));

        // Act & Assert
        mockMvc.perform(get("/clientes/{cpf}", cpf))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("11111111111"));
        verify(clienteService, times(1)).encontrarCliente(cpf);
    }

    @Test
    void testEncontrarCliente_NotFound() throws Exception {
        // Arrange
        when(clienteService.encontrarCliente(cliente.getCpf())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/clientes/{cpf}", cpf))
                .andExpect(status().isNotFound());
        verify(clienteService, times(1)).encontrarCliente(cpf);
    }

    @Test
    void testEncontrarTodos_Success() throws Exception {
        // Arrange
        when(clienteService.encontrarClientes()).thenReturn(List.of(cliente));

        // Act & Assert
        mockMvc.perform(get("/clientes/todos", cpf))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cpf").value("11111111111"));
        verify(clienteService, times(1)).encontrarClientes();
    }

    @Test
    void testEncontrarTodos_NotFound() throws Exception {
        // Arrange
        when(clienteService.encontrarClientes()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/clientes/todos", cpf))
                .andExpect(status().isNotFound());
        verify(clienteService, times(1)).encontrarClientes();
    }

    @Test
    void testCadastrarCliente_Success() throws Exception {
        // Arrange
        when(clienteService.cadastrarCliente(any(Cliente.class))).thenReturn(cliente);

        // Act & Assert
        mockMvc.perform(post("/clientes/cadastrar")
                        .contentType("application/json")
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(json));
        verify(clienteService, times(1)).cadastrarCliente(any(Cliente.class));
    }

    @Test
    void testCadastrarCliente_BadRequest() throws Exception {
        // Arrange
        when(clienteService.cadastrarCliente(any(Cliente.class))).thenThrow(new IllegalArgumentException("Dados inválidos"));

        // Act & Assert
        mockMvc.perform(post("/clientes/cadastrar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(clienteService, times(1)).cadastrarCliente(any(Cliente.class));
    }

    @Test
    void testAtualizarCliente_Success() throws Exception {
        // Arrange
        when(clienteService.encontrarCliente(cpf)).thenReturn(Optional.of(cliente));
        when(clienteService.alterarCliente(any(Cliente.class))).thenReturn(cliente);

        // Act & Assert
        mockMvc.perform(put("/clientes/alterar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().json(json));
        verify(clienteService, times(1)).alterarCliente(any(Cliente.class));
    }

    @Test
    void testAtualizarCliente_BadRequest() throws Exception {
        // Arrange
        when(clienteService.encontrarCliente(cpf)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/clientes/alterar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(clienteService, times(0)).alterarCliente(any(Cliente.class));
    }

    @Test
    void testDeletarCliente_Success() throws Exception {
        // Arrange
        when(clienteService.encontrarCliente(cliente.getCpf())).thenReturn(Optional.of(cliente));

        // Act & Assert
        mockMvc.perform(delete("/clientes/{cpf}", cpf))
                .andExpect(status().isNoContent());
        verify(clienteService, times(1)).deletarCliente(cpf);
    }

    @Test
    void testDeletarCliente_BadRequest() throws Exception {
        // Arrange
        when(clienteService.encontrarCliente(cliente.getCpf())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/clientes/{cpf}", cpf))
                .andExpect(status().isBadRequest());
        verify(clienteService, times(0)).deletarCliente(cpf);
    }

}

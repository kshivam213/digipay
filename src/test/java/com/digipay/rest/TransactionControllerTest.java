package com.digipay.rest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.digipay.rest.dtos.TransactionDto;
import com.digipay.services.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testMakeTransaction_Success() throws Exception {
        TransactionDto requestDto = new TransactionDto(); // Set required fields
        TransactionDto responseDto = new TransactionDto(); // Set response fields

        when(transactionService.makeTransaction(any(TransactionDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully made transaction"));

        verify(transactionService, times(1)).makeTransaction(any(TransactionDto.class));
    }
}

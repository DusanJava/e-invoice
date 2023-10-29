package com.einvoice.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getEInvoicesIds() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders.get("/invoices"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void downloadInvoices() throws Exception {
        List<Integer> invoiceIds = Arrays.asList(14348534,23500632,34211239);
        this.mvc.perform(MockMvcRequestBuilders.post("/invoices/download")
                                 .content(objectMapper.writeValueAsString(invoiceIds))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

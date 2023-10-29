package com.einvoice.controller;

import com.einvoice.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(final InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<String> getEInvoicesIds() {
        String ids = invoiceService.getEInvoicesIds();
        return ResponseEntity.ok(ids);
    }

    @PostMapping("/download")
    public void downloadInvoices(@RequestBody List<Integer> invoiceIds) {
        invoiceService.downloadInvoices(invoiceIds);
    }
}

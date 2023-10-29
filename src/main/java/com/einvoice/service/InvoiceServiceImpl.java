package com.einvoice.service;

import com.einvoice.client.InvoiceClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceClient invoiceClient;

    public InvoiceServiceImpl(final InvoiceClient invoiceClient) {
        this.invoiceClient = invoiceClient;
    }

    @Override
    public String getEInvoicesIds() {
        return invoiceClient.getEInvoicesIds();
    }

    @Override
    public void downloadInvoices(List<Integer> invoiceIds) {
        invoiceClient.downloadEInvoices(invoiceIds);
    }
}

package com.einvoice.service;

import java.util.List;

public interface InvoiceService {

    String getEInvoicesIds();

    void downloadInvoices(List<Integer> invoiceIds);
}

package com.einvoice.client;

import static java.lang.Thread.sleep;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class InvoiceClient {

    @Value("${e-invoice-ids-url}")
    private String idsUrl;

    @Value("${e-invoice-download-url}")
    private String downloadUrl;

    @Value("${api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public InvoiceClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getEInvoicesIds() {
        log.debug("Calling E invoice service, calling url: {}.", idsUrl);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("accept", "text/plain");
            headers.set("ApiKey", apiKey);
            HttpEntity entity = new HttpEntity(headers);

            HttpEntity<String> response = restTemplate.exchange(idsUrl, HttpMethod.POST, entity, String.class);
            System.out.println(response.getBody());
            return response.getBody();

        } catch (HttpServerErrorException e) {
            log.error("Cannot retrieve e invoice service, because of server error", e);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpClientErrorException e) {
            log.error("Cannot retrieve e invoice service, because of client error.");
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
    }

    public void downloadEInvoices(List<Integer> invoiceIds) {
        log.debug("Download E invoices from service, calling url: {}.", downloadUrl);

        invoiceIds.forEach(invoiceId -> {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.set("accept", "*/*");
                headers.set("ApiKey", apiKey);
                HttpEntity entity = new HttpEntity(headers);

                ResponseEntity<byte[]> response = restTemplate.exchange(downloadUrl + invoiceId,
                                                                        HttpMethod.GET,
                                                                        entity,
                                                                        byte[].class);

                String fileName = response.getHeaders().getContentDisposition().getFilename();
                assert fileName != null;
                final String cleanedName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");

                Files.write(Paths.get("Faktura" + cleanedName), Objects.requireNonNull(response.getBody()));
                log.info("Invoice with name: {} successfully downloaded.", cleanedName);
            } catch (HttpServerErrorException e) {
                log.error("Cannot retrieve e invoice service, because of server error", e);
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (HttpClientErrorException e) {
                log.error("Cannot retrieve e invoice service, because of client error for id: {}", invoiceId);
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}

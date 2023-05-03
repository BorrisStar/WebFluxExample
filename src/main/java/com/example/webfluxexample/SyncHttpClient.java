package com.example.webfluxexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class SyncHttpClient {
    private static final WebClient webClient = WebClient.builder().build();
    private static final String uri = "https://www.google.com";

    public static void main(String[] args) throws IOException, InterruptedException {
        int maxRequestNum = 100;

        HttpClient httpClient = HttpClient.newHttpClient();

        long startTime = System.currentTimeMillis();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();
        log.info("Request process started synchronously");
        for (int i = 0; i < maxRequestNum; i++) {
            httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        }

        log.info("Total time for downloading synchronously: {} milliseconds", System.currentTimeMillis() - startTime);


        startTime = System.currentTimeMillis();
        log.info("Request process started asynchronously but in synchro mode");
        Flux.range(0, maxRequestNum)
                .map(SyncHttpClient::sendRequestSynchronously)
                .blockLast();

        log.info("Total time for downloading synchronously with WebClient: {} milliseconds\n", System.currentTimeMillis() - startTime);
    }

    private static String sendRequestSynchronously(int i) {

        String result = webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
//                .doOnSuccess(response -> log.info("Send request number {} to google.de is OK.", i))
                .doOnError(error -> log.error("Send request number {} to google.de is not successful.", i, error))
                .block();

        return result;
    }
}

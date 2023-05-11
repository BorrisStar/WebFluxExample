package com.example.webfluxexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class PaginationFluxWebFluxClient {

    private static final WebClient webClient = WebClient.builder().build();

    private static final String uriBase = "http://localhost:8080/api/v1/webflux";


    public static void main(String[] args) {

        Mono.just("/r2dbc")
                .flatMap(PaginationFluxWebFluxClient::sendGetRequestAsynchronously)                 // Get Id
                .doOnSubscribe(sub -> log.info("Sending request to WebFluxExampleServer"))
                .doOnSuccess(response -> log.info("Response - \n{}", response))
                .doOnError(error -> {
                    log.error("Sending request to WebFluxExampleServer finished with exception {}\n", error.getMessage());
                })
                .block();

    }

    private static Mono<String> sendGetRequestAsynchronously(String subRequest) {

        return webClient
                .get()
                .uri(uriBase + subRequest)
                .retrieve()
                .bodyToMono(String.class);
    }
}

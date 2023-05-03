package com.example.webfluxexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
public class SequenceWebFluxClient {

    private static final WebClient webClient = WebClient.builder()
            .exchangeStrategies(ExchangeStrategies
                    .builder()
                    .codecs(codecs -> codecs
                            .defaultCodecs()
                            .maxInMemorySize(500 * 1024))
                    .build())
            .build();

    private static final String uriBase = "http://localhost:8080/api/v1/webflux";


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();
        log.info("Request process started asynchronously");

        Mono.just("/id")
                .flatMap(SequenceWebFluxClient::sendGetRequestAsynchronously)                 // Get Id
                .flatMap(body -> sendPostRequestAsynchronously("/id", body))        // Send Id -> get Name
                .flatMap(body -> sendPostRequestAsynchronously("/name", body))      // Send Name -> get Position
                .flatMap(body -> sendPostRequestAsynchronously("/position", body))  // Send Position -> get Salary
                .doOnSubscribe(sub -> log.info("Sending request to WebFluxExampleServer"))
                .doOnSuccess(response -> log.info("Response - Salary = {} - received with success", response))
                .doOnError(error -> {
                    log.error("Sending request to WebFluxExampleServer finished with exception {}\n", error.getMessage());
                })
                .block();

        log.info("Total time for downloading asynchronously with WebClient: {} milliseconds\n", System.currentTimeMillis() - startTime);
    }


    private static Mono<String> sendPostRequestAsynchronously(String subRequest, String body) {

        return webClient
                .post()
                .uri(uriBase + subRequest)
                .body(BodyInserters.fromValue(body))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class));
    }

    private static Mono<String> sendGetRequestAsynchronously(String subRequest) {

        return webClient
                .get()
                .uri(uriBase + subRequest)
                .retrieve()
                .bodyToMono(String.class);
    }
}

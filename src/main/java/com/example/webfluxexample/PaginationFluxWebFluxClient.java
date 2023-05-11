package com.example.webfluxexample;

import com.example.webfluxexample.domain.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class PaginationFluxWebFluxClient {

    private static final WebClient webClient = WebClient.builder().build();

    private static final String uriBase = "http://localhost:8080/api/v1/webflux/r2dbc";


    public static void main(String[] args) {

        Message newMessage = new Message();

        sendGetRequestAsynchronously()                 // Get Messages
                .doOnSubscribe(sub -> log.info("Sending request to WebFluxExampleServer"))
                .doOnSuccess(response -> log.info("Response - \n{}", response))
                .doOnError(error -> {
                    log.error("Sending request to WebFluxExampleServer finished with exception {}\n", error.getMessage());
                })
                .block();

    }

    private static Mono<List<Message>> sendGetRequestAsynchronously() {

        return webClient
                .get()
                .uri(uriBase)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    private static Mono<Void> sendPostRequestAsynchronously(Message body) {

        return webClient
                .post()
                .uri(uriBase)
                .body(BodyInserters.fromValue(body))
                .exchangeToMono(new Void.class);
    }

    private static Mono<String> sendNextMessage(List<Message> messages) {
        long lastId = messages.stream().reduce((first, last) -> last).orElse(new Message(0L,"")).getId();
        return sendPostRequestAsynchronously(new Message(lastId+1,"New Data");
    }
}

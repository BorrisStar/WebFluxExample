package com.example.webfluxexample.controller;

import com.example.webfluxexample.domain.Message;
import com.example.webfluxexample.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reactive")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @GetMapping
    public Flux<Message> getAll() {
        return messageService.getAll();
    }

    @PostMapping
    public Mono<Message> addOne(@RequestBody Message message) {
        return messageService.addOne(message);
    }
}

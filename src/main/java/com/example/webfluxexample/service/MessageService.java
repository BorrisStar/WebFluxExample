package com.example.webfluxexample.service;

import com.example.webfluxexample.domain.Message;
import com.example.webfluxexample.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public Flux<Message> getAll() {
        return messageRepository.findAll();
    }

    @Transactional
    public Mono<Message> addOne(Message message) {
        return messageRepository.save(message);
    }
}

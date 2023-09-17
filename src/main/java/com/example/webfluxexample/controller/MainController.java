package com.example.webfluxexample.controller;

import com.example.webfluxexample.model.NewsEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    private final ApplicationEventPublisher eventPublisher;

    public MainController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/event")
    public ResponseEntity<String> satEvent(@RequestParam String news) {
        eventPublisher.publishEvent(new NewsEvent(this, news));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

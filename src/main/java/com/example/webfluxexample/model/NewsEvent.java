package com.example.webfluxexample.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
@ToString
public class NewsEvent extends ApplicationEvent {
    private final String news;
    private final LocalDateTime time;

    public NewsEvent(Object source, String news) {
        super(source);
        this.news = news;
        this.time = LocalDateTime.now();
    }
}

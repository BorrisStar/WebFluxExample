package com.example.webfluxexample.service;

import com.example.webfluxexample.model.NewsEvent;
import com.example.webfluxexample.subscriber.Consumer;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SpigelService implements ApplicationListener<NewsEvent>, Consumer {
    @Override
    public void onApplicationEvent(NewsEvent event) {
        handelEvent(event);
    }

    @Override
    public void handelEvent(NewsEvent event) {
        System.out.printf("Spigel received new news: %s%n", event);
    }
}

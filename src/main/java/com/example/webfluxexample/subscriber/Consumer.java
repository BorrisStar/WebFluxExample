package com.example.webfluxexample.subscriber;

import com.example.webfluxexample.model.NewsEvent;

public interface Consumer {
    void handelEvent(NewsEvent newsEvent);
}

package com.example.webfluxexample;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class WebFluxExampleApplicationTests {

    @Test
    public void fluent_long_to_times_slower_then_short() {
        var shortTiming = IntStream.range(0, 1000).mapToLong(i-> short_fluent_timing_old_style()).sum();
        var longTiming = IntStream.range(0, 1000).mapToLong(i-> long_fluent_timing_functional_style()).sum();

       assertTrue((2 > (longTiming / shortTiming)));
    }

    // 1 reactive fluent
    private long short_fluent_timing_old_style() {
        var startedTime = System.currentTimeMillis();
        var i10List = new ArrayList<Integer>();
        var i100List = new ArrayList<Integer>();

        StepVerifier.create(Flux.range(1, 100_000)
                .handle((i, s) -> {
                    var ix10 = i * 10;
                    if (ix10 % 10 != 0) {
                        return;
                    }
                    log.debug("ix10:" + ix10);
                    i10List.add(i);

                    var ix100 = ix10 * 10;
                    if (ix100 % 100 != 0) {
                        return;
                    }
                    log.debug("ix100:" + ix100);
                    i100List.add(i);

                    var ix200 = ix100 * 2;
                    if (ix200 > 1000_000_000) {
                        log.info("i > 1000_000_000:" + i);
                        s.next(ix200);
                    }
                })
        ).verifyComplete();

        return System.currentTimeMillis() - startedTime;
    }

    private long long_fluent_timing_functional_style() {
        var startedTime = System.currentTimeMillis();
        var i10List = new ArrayList<Integer>();
        var i100List = new ArrayList<Integer>();

        StepVerifier.create(Flux.range(1, 100_000)
                .map(i -> i * 10)
                .doOnNext(i -> {
                    log.debug("ix10:" + i);
                    i10List.add(i);
                })
                .filter(i -> i % 10 == 0)
                .map(i -> i * 10)
                .filter(i -> i % 100 == 0)
                .doOnNext(i -> {
                    log.debug("ix100:" + i);
                    i100List.add(i);
                })
                .map(i -> i * 2)
                .filter(i -> i > 1000_000_000)
                .doOnNext(i ->
                        log.info("i > 1000_000_000:" + i)
                )
        ).verifyComplete();

        return System.currentTimeMillis() - startedTime;
    }
}

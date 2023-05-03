package com.example.webfluxexample;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.stream.Collectors;

@SpringBootTest
class WebFluxExampleApplicationTests {

    @Test
    public void collectMultiMap() {
        Flux.just("one", "two", "three", "four", "five", "six", "seven", "ten")
                .collectMultimap(a -> a.charAt(0), b -> b)
                .subscribe(System.out::println);
    }

    @Test
    public void collectToList() {
        Flux.just("one", "two", "three", "four", "five")
                .map(s -> s.charAt(0) + "_" + s )
                .collect(Collectors.toList())
                .subscribe(System.out::println);
    }

    @Test
    public void monoWithValueInsteadOfError() {
        var mono  = betterCallDefaultForErrorMono(Mono.error(new IllegalStateException()));

        StepVerifier.create(mono)
                .expectNext("Default")
                .verifyComplete();

        mono = betterCallDefaultForErrorMono(Mono.just("Next"));

        StepVerifier.create(mono)
                .expectNext("Next")
                .verifyComplete();
    }

    Mono<String> betterCallDefaultForErrorMono(Mono<String> mono) {
        return mono.onErrorResume(e -> Mono.just("Default"));
    }

    @Test
    void zipExample() {
        Flux<String> fluxFruits = Flux.just("apple", "pear", "plum");
        Flux<String> fluxColors = Flux.just("red", "green", "blue");
        Flux<Integer> fluxAmounts = Flux.just(10, 20, 30);
        Flux.zip(fluxFruits, fluxColors, fluxAmounts).subscribe(System.out::println);
    }

    @Test
    void mapExample() {
        Flux<String> fluxColors = Flux.just("red", "green", "blue");
        fluxColors.map(color -> color.charAt(0)).subscribe(System.out::println);
    }

    @Test
    void flatMapExample() {
        Flux<String> fluxColors = Flux.just("red", "green", "blue");
        fluxColors.flatMap(this::countSymbols).subscribe(System.out::println);
    }
    Flux<Integer> countSymbols(String str){
        return Flux.just(str.length());
    }

    @Test
    public void onErrorExample() {
        Flux<String> fluxCalc = Flux.just(-1, 0, 1)
                .map(i -> "10 / " + i + " = " + (10 / i));

        fluxCalc.subscribe(value -> System.out.println("Next: " + value),
                error -> System.err.println("Error: " + error));
    }
    @Test
    public void fluxWithDoOnPrintln() {
        Flux<String> fluxColors = Flux.just("red", "green", "blue");
        fluxColors
                .doOnSubscribe(s->System.out.println("start"))
                .doOnNext(System.out::println)
                .doOnComplete(()->System.out.println("end!\n"))
                .toIterable()
                .forEach(System.out::println);

        fluxColors
                .doOnSubscribe(s->System.out.println("\nstart2"))
                .doOnNext(System.out::println)
                .doOnComplete(()->System.out.println("end2!"))
                .subscribe();
    }

}

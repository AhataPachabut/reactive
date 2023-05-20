package com.epam.reactive;

import com.epam.reactive.dto.SportDto;
import com.epam.reactive.dto.SportsDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class SportWebClient {

    private final WebClient client;

    public SportWebClient(WebClient.Builder builder) {
        this.client = builder
            .baseUrl("https://sports.api.decathlon.com/sports")
            .exchangeStrategies(ExchangeStrategies
                .builder()
                .codecs(codecs -> codecs
                    .defaultCodecs()
                    .maxInMemorySize(5000 * 1024))
                .build())
            .build();
    }

    public Flux<SportDto> getSports() {
        return this.client.get()
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(SportsDto.class)
            .flatMapIterable(SportsDto::getData);
    }
}

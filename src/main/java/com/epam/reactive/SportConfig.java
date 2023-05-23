package com.epam.reactive;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.epam.reactive.entity.Sport;
import com.epam.reactive.repository.SportRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class SportConfig {

    @Bean
    public RouterFunction<ServerResponse> route(SportRepository sportRepository, SportWebClient sportWebClient) {
        return RouterFunctions
            .route()
            .path("/api/v1/sport", builder -> builder
                .POST("/{sportname}",
                    accept(MediaType.APPLICATION_JSON),
                    request -> {
                        String sportName = request.pathVariable("sportName");
                        return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(sportRepository.save(Sport.builder().name(sportName).build()), Sport.class);
                    })
                .GET("",
                    accept(MediaType.APPLICATION_JSON),
                    request -> {
                        String sportName = request.queryParam("sportName").orElseThrow();
                        return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(sportRepository.findByName(sportName), Sport.class);
                    }
                )
            ).build();
    }

    @Bean
    public CommandLineRunner demo(SportRepository sportRepository, SportWebClient sportWebClient) {
        return args -> sportRepository.saveAll(
            sportWebClient.getSports().map(sportDto ->
                Sport.builder()
                    .name(sportDto.getName())
                    .build()
            )).blockLast();
    }
}

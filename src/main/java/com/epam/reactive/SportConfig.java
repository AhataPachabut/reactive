package com.epam.reactive;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.epam.reactive.entity.Sport;
import com.epam.reactive.repository.SportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
@Slf4j
public class SportConfig {

    @Bean
    public RouterFunction<ServerResponse> route(SportRepository sportRepository) {
        return RouterFunctions
            .route()
            .path("/api/v1/sport", builder ->
                builder
                    .POST("/{sportName}",
                        accept(MediaType.APPLICATION_JSON),
                        request -> {
                            String sportName = request.pathVariable("sportName");
                            return sportRepository.save(Sport.builder().name(sportName).build())
                                .flatMap(sport -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(sport, Sport.class)
                                ).onErrorResume(e -> {
                                    log.error("Unable to create sport: " + sportName, e);
                                    return ServerResponse.badRequest().build();
                                });
                        })
                    .GET("",
                        accept(MediaType.APPLICATION_JSON),
                        request -> {
                            String sportName = request.queryParam("sportName").orElseThrow();
                            return sportRepository.findByName(sportName)
                                .flatMap(sport -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(sport, Sport.class)
                                ).switchIfEmpty(ServerResponse.notFound().build());
                        })
            ).build();
    }

    @Bean
    public CommandLineRunner demo(SportRepository sportRepository, SportWebClient sportWebClient) {
        return args -> sportRepository.saveAll(
            sportWebClient.getSports().map(sportDto ->
                Sport.builder()
                    .name(sportDto.getName())
                    .build()
            )
        ).onErrorContinue((throwable, o) ->
            log.error("Unable to create sport", throwable)
        ).blockLast();
    }
}

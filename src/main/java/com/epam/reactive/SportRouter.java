package com.epam.reactive;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.epam.reactive.entity.Sport;
import com.epam.reactive.repository.SportRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class SportRouter {

    @Bean
    public RouterFunction<ServerResponse> route(SportRepository sportRepository) {

        return RouterFunctions
            .route()
            .path("/api/v1/sport", builder -> builder
                .POST("/{sportname}",
                    accept(MediaType.APPLICATION_JSON),
                    request -> {
                        String sportName = request.pathVariable("sportname");
                        return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(sportRepository.save(Sport.builder().name(sportName).build()), Sport.class);
                    })
                .GET("",
                    accept(MediaType.APPLICATION_JSON),
                    request -> {
                        String sportName = request.queryParam("sportname").orElseThrow();
                        return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(sportRepository.findByName(sportName), Sport.class);
                    }
                )
            ).build();
    }
}

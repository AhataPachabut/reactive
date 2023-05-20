package com.epam.reactive;

import com.epam.reactive.entity.Sport;
import com.epam.reactive.repository.SportRepository;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class Consumer implements SmartLifecycle {

    private SportWebClient sportWebClient;
    private SportRepository sportRepository;

    public Consumer(SportWebClient sportWebClient, SportRepository sportRepository) {
        this.sportWebClient = sportWebClient;
        this.sportRepository = sportRepository;
    }

    @Override
    public void start() {
        sportWebClient.getSports()
            .doOnNext(sportDto -> {
                sportRepository.save(
                    Sport.builder()
                        .id(sportDto.getId())
                        .name(sportDto.getAttributes().getName())
                        .build());
            }).blockLast();
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}

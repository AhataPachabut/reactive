package com.epam.reactive.repository;

import com.epam.reactive.entity.Sport;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SportRepository extends ReactiveCrudRepository<Sport, Integer> {

    Mono<Sport> findByName(String name);
}

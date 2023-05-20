package com.epam.reactive.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table("sports")
public class Sport {
    @Id
    private Integer id;
    private final String name;
}

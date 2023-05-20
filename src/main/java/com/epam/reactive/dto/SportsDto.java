package com.epam.reactive.dto;

import java.util.Collection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SportsDto {
    private Collection<SportDto> data;
}

package com.abnamro.recipe.domain.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageFilters {

    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;
}

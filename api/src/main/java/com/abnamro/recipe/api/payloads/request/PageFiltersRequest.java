package com.abnamro.recipe.api.payloads.request;

import com.abnamro.recipe.domain.filter.PageFilters;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageFiltersRequest {

    @ApiModelProperty(notes = "Starting page", example = "1")
    @Builder.Default
    @Min(1L)
    @NotNull
    private Integer page = 1;

    @ApiModelProperty(notes = "Pages size", example = "10")
    @Builder.Default
    @Min(1L)
    @Max(1000L)
    @NotNull
    private Integer size = 10;

    public PageFiltersRequest(PageFilters pageFilters) {
        this.page = pageFilters.getPage();
        this.size = pageFilters.getSize();
    }

    public PageFilters toEntity() {
        return PageFilters.builder()
                .page(page)
                .size(size)
                .build();
    }
}

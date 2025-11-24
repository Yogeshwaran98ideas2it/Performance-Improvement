package com.acme.healthcare.common.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic paged response wrapper to standardize cross-service pagination.
 *
 * @param <T> element type
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponseDto<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
}











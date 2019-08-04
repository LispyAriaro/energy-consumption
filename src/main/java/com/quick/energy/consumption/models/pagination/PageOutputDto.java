package com.quick.energy.consumption.models.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * @author bodmas
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PageOutputDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Serializable> elements;
    private int totalPages;
    private int currentPageNumber;

    public PageOutputDto() {
    }

    public PageOutputDto(Page pageable) {
        this.elements = pageable.getContent();
        this.totalPages = pageable.getTotalPages();
        this.currentPageNumber = pageable.getNumber() + 1;
    }

    public List<Serializable> getElements() {
        return elements;
    }

    public void setElements(List<Serializable> elements) {
        this.elements = elements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }
}

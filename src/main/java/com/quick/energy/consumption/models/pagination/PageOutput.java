package com.quick.energy.consumption.models.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * @author bodmas
 * @param <T>
 */
public class PageOutput<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<T> elements;
    private final int totalPages;
    private final int currentPageNumber;

    public PageOutput(List<T> elements, int totalPages, int currentPageNumber) {
        this.elements = elements;
        this.totalPages = totalPages;
        this.currentPageNumber = currentPageNumber;
    }

    public List<T> getElements() {
        return elements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getCurrentPageNumber() {
        return currentPageNumber;
    }


    public Page getPage() {
        return new Page() {
            @Override
            public int getTotalPages() {
                return PageOutput.this.getTotalPages();
            }

            @Override
            public long getTotalElements() {
                return PageOutput.this.elements.size();
            }

            @Override
            public Page map(Function converter) {
                return null;
            }

            @Override
            public int getNumber() {
                return PageOutput.this.getCurrentPageNumber() -1 ;
            }

            @Override
            public int getSize() {
                return PageOutput.this.getElements().size();
            }

            @Override
            public int getNumberOfElements() {
                return PageOutput.this.getElements().size();
            }

            @Override
            public List getContent() {
                return PageOutput.this.getElements();
            }

            @Override
            public boolean hasContent() {
                return PageOutput.this.getElements().size() > 0;
            }

            @Override
            public Sort getSort() {
                return null;
            }

            @Override
            public boolean isFirst() {
                return false;
            }

            @Override
            public boolean isLast() {
                return false;
            }

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Pageable nextPageable() {
                return null;
            }

            @Override
            public Pageable previousPageable() {
                return null;
            }

            @Override
            public Iterator iterator() {
                return PageOutput.this.getElements().iterator();
            }
        };
    }
}

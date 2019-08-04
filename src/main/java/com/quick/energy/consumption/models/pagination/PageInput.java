package com.quick.energy.consumption.models.pagination;

import java.io.Serializable;

/**
 * @author bodmas
 */
public class PageInput implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * One-indexed page number.
     */
    private final int page;

    /**
     * Number of items in each page.
     */
    private final int size;

    public PageInput(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}

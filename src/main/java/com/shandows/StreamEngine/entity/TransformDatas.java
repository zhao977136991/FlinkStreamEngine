package com.shandows.StreamEngine.entity;

import java.util.List;

public class TransformDatas {
    private String name;
    private String method;
    private List<ToColumns> toColumns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<ToColumns> getToColumns() {
        return toColumns;
    }

    public void setToColumns(List<ToColumns> toColumns) {
        this.toColumns = toColumns;
    }
}

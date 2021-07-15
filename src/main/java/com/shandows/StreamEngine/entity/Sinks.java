package com.shandows.StreamEngine.entity;

import java.util.List;

public class Sinks {
    private List<String> createTables;
    private List<String> insertSQLs;

    public List<String> getCreateTables() {
        return createTables;
    }

    public void setCreateTables(List<String> createTables) {
        this.createTables = createTables;
    }

    public List<String> getInsertSQLs() {
        return insertSQLs;
    }

    public void setInsertSQLs(List<String> insertSQLs) {
        this.insertSQLs = insertSQLs;
    }
}

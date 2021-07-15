package com.shandows.StreamEngine.config;

public class StaticVariables {
    public interface FieldType{
        String STRING = "String";
        String BIGINT = "BIGINT";
        String INT = "INT";

    }
    public interface Location{
        String KEY = "key";
        String VALUE = "value";
        String ADD = "add";
    }

    public interface DataType{
        String STRING = "STRING";
        String BIGINT = "BIGINT";
        String MAP_STRING_STRING = "MAP<STRING, STRING>";
    }
    public static final String DEFAULTJOBNAME = "flinkStreamEngineJob";
}

package com.shandows.StreamEngine.config;


import com.shandows.StreamEngine.entity.Engine;

public abstract class EngineParse {
    private Engine engine;

    public EngineParse(Engine engine) {
        this.engine = engine;
    }

    public EngineParse() {
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}

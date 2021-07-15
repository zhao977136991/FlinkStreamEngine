package com.shandows.StreamEngine.config;

import com.alibaba.fastjson.JSON;
import com.shandows.StreamEngine.entity.Engine;
import com.shandows.StreamEngine.util.FileUtils;
import org.apache.commons.io.FileExistsException;

public class FlinkStreamEngineFileParse extends EngineParse {
    public FlinkStreamEngineFileParse(Engine engine) {
        super(engine);
    }

    public FlinkStreamEngineFileParse(String path) {
        if (FileUtils.isExists(path)){
            String fileJsonString = FileUtils.readText(path);
            Engine engine = JSON.parseObject(fileJsonString,Engine.class);
            this.setEngine(engine);
        }else {
            new FileExistsException("path File not exist");
        }
    }

    public FlinkStreamEngineFileParse() {
    }
}

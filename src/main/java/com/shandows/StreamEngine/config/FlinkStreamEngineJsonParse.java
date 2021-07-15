package com.shandows.StreamEngine.config;

import com.alibaba.fastjson.JSON;
import com.shandows.StreamEngine.entity.Engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;

public class FlinkStreamEngineJsonParse extends EngineParse {

    public FlinkStreamEngineJsonParse(Engine engine) {
        super(engine);
    }

    public FlinkStreamEngineJsonParse(String engineJson) {
        Engine engine = JSON.parseObject(engineJson,Engine.class);
        this.setEngine(engine);

    }

    public FlinkStreamEngineJsonParse() {
    }

    public String getConfigFile(String filePath){
            StringBuilder result = new StringBuilder();
            try{
                BufferedReader br = new BufferedReader(new FileReader(filePath));//构造一个BufferedReader类来读取文件
                String s = null;
                while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                    result.append(System.lineSeparator()+s);
                }
                br.close();
            }catch(Exception e){
                e.printStackTrace();
            }
            return result.toString();
    }

    @Override
    public Engine getEngine() {
        return super.getEngine();
    }

    @Override
    public void setEngine(Engine engine) {
        super.setEngine(engine);
    }

    public Engine jsonToEntity(String engineJson){
        Engine engine = JSON.parseObject(engineJson,Engine.class);
        return engine;
    }

    public static void main(String[] args) {
        String resource = "aim_serviceToCh.json";
        URL resource2 = Thread.currentThread().getContextClassLoader().getResource(resource);
        System.out.println(resource2.getPath());
        FlinkStreamEngineJsonParse flinkStreamEngineJsonParse = new FlinkStreamEngineJsonParse();
        String json = flinkStreamEngineJsonParse.getConfigFile(resource2.getPath());
        flinkStreamEngineJsonParse.jsonToEntity(json);
    }
}

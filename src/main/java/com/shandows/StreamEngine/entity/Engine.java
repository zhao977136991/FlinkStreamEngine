package com.shandows.StreamEngine.entity;


import com.shandows.StreamEngine.config.StaticVariables;

import java.io.Serializable;
import java.util.List;

public class Engine implements Serializable {
    private String jobName;
    private List<String> createTables;
    private List<Transform> transforms;
    private List<Sinks> sinks;

    public String getJobName() {
        if (jobName==null||jobName.equals("")){
            return StaticVariables.DEFAULTJOBNAME;
        }else {
            return jobName;
        }
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public List<String> getCreateTables() {
        return createTables;
    }

    public void setCreateTables(List<String> createTables) {
        this.createTables = createTables;
    }

    public List<Transform> getTransforms() {
        return transforms;
    }

    public void setTransforms(List<Transform> transforms) {
        this.transforms = transforms;
    }

    public List<Sinks> getSinks() {
        return sinks;
    }

    public void setSinks(List<Sinks> sinks) {
        this.sinks = sinks;
    }
}

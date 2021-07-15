package com.shandows.StreamEngine.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Transform{
    private String querySQL;
    private List<TransformDatas> transformDatas;
    private String tableName;

    public String getQuerySQL() {
        return querySQL;
    }

    public void setQuerySQL(String querySQL) {
        this.querySQL = querySQL;
    }

    public List<TransformDatas> getTransformDatas() {
        return transformDatas;
    }

    public void setTransformDatas(List<TransformDatas> transformDatas) {
        this.transformDatas = transformDatas;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public static void main(String[] args) {
        List<Transform> transforms = new ArrayList<>();
        Transform transform = new Transform();
        Transform transform2 = new Transform();
        transform.setQuerySQL("sss");
        transform.setTableName("aaa");
        List<TransformDatas> transformDatasList = new ArrayList<>();
        TransformDatas transformDatas1 = new TransformDatas();
        transformDatas1.setMethod("bb");
        transformDatasList.add(transformDatas1);
        transform.setTransformDatas(transformDatasList);
        transforms.add(transform);
        System.out.println(JSONObject.toJSONString(transforms));
    }
}

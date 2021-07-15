package com.shandows.StreamEngine.config;


import com.shandows.StreamEngine.entity.ToColumns;

import java.math.BigDecimal;
import java.util.List;

public class MethodParse {

    //获取解析值，24，若值有小数25.1 则返回251，同时factor 设置为10
    public static Object getDataValueByKey(List<ToColumns> toColumnsList, Object dataValue, String key, String type){
        for (ToColumns toColumn: toColumnsList){
            if (toColumn.getLocation().equals(type)){
                String valueType = toColumn.getType();
                if (valueType.equals(StaticVariables.FieldType.STRING)){
                    return dataValue.toString();
                }else if (valueType.equals(StaticVariables.FieldType.BIGINT)){
                    if (dataValue.toString().indexOf(".")!=-1){
                        BigDecimal bd=new BigDecimal(String.valueOf(dataValue));
                        long retV = (long) (bd.doubleValue()*Math.pow(10, bd.scale()));
                        return retV;
                    }else {

                        Long retValue = Long.valueOf(dataValue.toString());
                        return retValue;
                    }
                }
            }
        }
        return null;
    }
    //获取解析值，24，若值有小数25.1 则返回251，同时factor 设置为10
    public static Integer getDataFactorByKey(Object dataValue){
        BigDecimal bd=new BigDecimal(String.valueOf(dataValue));
        return (int)Math.pow(10, bd.scale());
    }

}

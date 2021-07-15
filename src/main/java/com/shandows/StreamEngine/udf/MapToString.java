package com.shandows.StreamEngine.udf;

import org.apache.flink.table.functions.ScalarFunction;

import java.util.HashMap;
import java.util.Map;

public class MapToString extends ScalarFunction {


    public static String eval(Map<String,String> map){
        StringBuffer sb = new StringBuffer();
        if (map!=null&&map.size()>0){
            for (String key:map.keySet()){
                sb.append(key).append(",").append(map.get(key)).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put("1","aa");
        map.put("2","bb");
        map.put("3","cc");
        System.out.println(MapToString.eval(map));
    }
}

package com.shandows.StreamEngine.udf;

import org.apache.flink.table.functions.ScalarFunction;

import java.util.HashMap;
import java.util.Map;

public class MessageDataToKVS extends ScalarFunction {

    public static final String KTYPE = "K";
    public static final String VTYPE = "V";
    public static String eval(Map<String,String> map,String kvType,Integer number){
        if (map!=null&&map.size()>0){
            String[] keys = new String[map.size()];
            map.keySet().toArray(keys);
            if (kvType.equals(KTYPE)){
                return keys[number];
            }
            if (kvType.equals(VTYPE)){
                return map.get(keys[number]);
            }else {
                return null;
            }

        }else {
            return null;

        }
    }

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put("1","a");
        map.put("2","b");
        map.put("3","c");
        map.put("4","d");
        map.put("5","e");
        MessageDataToKVS messageDataToKVS = new MessageDataToKVS();
        for (int i = 0;i<map.size();i++){
            System.out.println(messageDataToKVS.eval(map,MessageDataToKVS.KTYPE,i));
            System.out.println(messageDataToKVS.eval(map,MessageDataToKVS.VTYPE,i));

        }
    }
}

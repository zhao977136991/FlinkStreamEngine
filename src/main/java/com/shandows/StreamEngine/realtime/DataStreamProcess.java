package com.shandows.StreamEngine.realtime;

import com.shandows.StreamEngine.config.MethodParse;
import com.shandows.StreamEngine.config.StaticVariables;
import com.shandows.StreamEngine.entity.ToColumns;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;

import java.util.List;
import java.util.Map;

public class DataStreamProcess {

    private static TypeInformation[] typeProcess(TableSchema tableSchema, List<ToColumns> toColumnsList){
        //  2021/3/23 类型转换
        TypeInformation[] types = dataTypesTranform(tableSchema.getFieldDataTypes(),toColumnsList);
        return types;
    }

    private static TypeInformation[] dataTypesTranform(DataType[] dataTypes, List<ToColumns> toColumnsList){
        if (dataTypes!=null&&dataTypes.length>0){
            int typeLen = dataTypes.length+toColumnsList.size();
            TypeInformation[] typeInformations = new TypeInformation[typeLen];
            for (int i = 0;i<dataTypes.length;i++){
                if (dataTypes[i].toString().equals(StaticVariables.DataType.STRING)){
                    typeInformations[i] = Types.STRING;
                }else if (dataTypes[i].toString().equals(StaticVariables.DataType.BIGINT)){
                    typeInformations[i] = Types.LONG;
                }else if (dataTypes[i].toString().equals(StaticVariables.DataType.MAP_STRING_STRING)){
                    typeInformations[i] = Types.MAP(Types.STRING, Types.STRING);
                }
            }
            if (null!=toColumnsList &&toColumnsList.size()>0){
                for (int i = 0; i < toColumnsList.size(); i++) {
                    if (toColumnsList.get(i).getType().equals(StaticVariables.FieldType.STRING)){
                        typeInformations[dataTypes.length+i] = Types.STRING;
                    }else if (toColumnsList.get(i).getType().equals(StaticVariables.FieldType.BIGINT)){
                        typeInformations[dataTypes.length+i] = Types.LONG;
                    } else if (toColumnsList.get(i).getType().equals(StaticVariables.FieldType.INT)){
                        typeInformations[dataTypes.length+i] = Types.INT;
                    }

                }
            }
            return typeInformations;
        }else {
            return null;
        }
    }

    private static String[] getFieldName(TableSchema tableSchema,List<ToColumns> toColumnsList){
        String[] fs = tableSchema.getFieldNames();
        int fiedLen = fs.length+toColumnsList.size();
        String[] retField = new String[fiedLen];
        for (int i = 0;i<fs.length;i++){
            retField[i] = fs[i];
        }
        int len = fs.length;
        for (ToColumns toColumn:toColumnsList){
            retField[len++] = toColumn.getName();
        }
        return retField;
    }


    public static DataStream<Row> oneToMultiRow(StreamTableEnvironment bsTableEnv, Table table, String transformName, List<ToColumns> toColumns ){
        DataStream<Row> dataStream = bsTableEnv.toAppendStream(table, Row.class);
        TypeInformation[] typeInformations = typeProcess(table.getSchema(),toColumns);
        String[] newField = getFieldName(table.getSchema(),toColumns);
        String[] field = table.getSchema().getFieldNames();
        DataStream<Row> ds = dataStream.flatMap(new FlatMapFunction<Row, Row>() {
            @Override
            public void flatMap(Row value, Collector<Row> out) throws Exception {
                for (int i=0;i<value.getArity();i++){
                    if (transformName.equals(field[i])){
                        Map dataMap = (Map) value.getField(i);
//                        // 解析data 封装为新的 Row 同时 设置Feild
                        for (int dataIndex = 0;dataIndex<dataMap.size();dataIndex++){
                            Row addRow = new Row(toColumns.size());
                            Object[] dataKeys = dataMap.keySet().toArray();
                            for (int keyIndex = 0;keyIndex<dataKeys.length;keyIndex++){
                                String dataName = (String) dataKeys[i];
                                Object dataValue = dataMap.get(dataName);
//                                //设置name
                                addRow.setField(0,dataName);
//                                //设置value
                                Object v = MethodParse.getDataValueByKey(toColumns,dataValue,field[i], StaticVariables.Location.VALUE);
                                addRow.setField(1,v);
                                //设置拓展字段
                                Integer factor = MethodParse.getDataFactorByKey(dataValue);
                                addRow.setField(2,factor);
                                Row outRow = Row.join(value,addRow);
                                out.collect(outRow);
                            }
                        }
                    }
                }
            }
        }).returns(new RowTypeInfo(typeInformations,newField));
        return ds;
    }
}

package com.shandows.StreamEngine.realtime;

import com.shandows.StreamEngine.config.AutoRegisterUDF;
import com.shandows.StreamEngine.entity.Engine;
import com.shandows.StreamEngine.entity.Sinks;
import com.shandows.StreamEngine.config.FlinkStreamEngineFileParse;
import com.shandows.StreamEngine.entity.TransformDatas;
import com.shandows.StreamEngine.config.EngineParse;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.util.List;

/**
 *  * @author ：shandows
 *  * @date ：Created in 3/23/2021 7:28 PM
 *  * @description：
 *  * @modified By：
 *  * @version: 1.0
 */
public class FlinkStreamEngine {

    public static void main(String[] args) throws Exception {

        // 2021/3/23 1.加载Stream环境和table环境
        StreamExecutionEnvironment bsEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        bsEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        EnvironmentSettings bsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment bsTableEnv = StreamTableEnvironment.create(bsEnv, bsSettings);
        // 2021/3/23 2.自动加载UDF函数
        AutoRegisterUDF.autoRegisterUDF(bsTableEnv);
        // 2021/3/23 3.获取配置文件，解析出createTables、transform、sinks
//        EngineParse engineParse = new FlinkStreamEngineJsonParse(json);
        String path = "F:\\coding\\asia\\flink-dataops\\src\\main\\resources\\aim_serviceToCh.json";
        EngineParse engineParse = new FlinkStreamEngineFileParse(path);
        Engine engine = engineParse.getEngine();
        //  2021/3/23 4.执行createTables
        executeCreateTablesSqls(bsTableEnv,engine);
        //  2021/3/23 5.执行自定义transform语句
        executeTransforms(bsTableEnv,engine);
        //  2021/3/23 6.执行sinks
        executeSinks(bsTableEnv,engine);
        bsEnv.execute(engine.getJobName());
    }

    private static void executeCreateTablesSqls(StreamTableEnvironment bsTableEnv,Engine engine){
        if (null!=engine&& null!=engine.getCreateTables()
                &&engine.getCreateTables().size()>0){
            engine.getCreateTables().forEach(createTable->{
                bsTableEnv.executeSql(createTable);
            });
        }
    }

    private static void executeTransforms(StreamTableEnvironment bsTableEnv,Engine engine){
        if (null!=engine&& null!=engine.getTransforms()
        && engine.getTransforms().size()>0){
//            List<Transform> transforms = new ArrayList<>();
            engine.getTransforms().forEach(transform -> {
                Table table = bsTableEnv.sqlQuery(transform.getQuerySQL());
                //输出
                DataStream<Row> dataStream = bsTableEnv.toAppendStream(table, Row.class);
                dataStream.print("table");
                List<TransformDatas> transformDatas = transform.getTransformDatas();
                if (null!=transformDatas&&transformDatas.size()>0){
                    transformDatas.forEach(transformData->{
                        // TODO: 2021/3/23 后期扩展自定义解析函数
                        String method = transformData.getMethod();
                        DataStream<Row> ds = DataStreamProcess.oneToMultiRow(bsTableEnv,table,transformData.getName(),transformData.getToColumns());
                        ds.print("transform");
                        bsTableEnv.createTemporaryView(transform.getTableName(),ds);
                    });
                }
            });
        }
    }


    private static void executeSinks(StreamTableEnvironment bsTableEnv,Engine engine){
        if (null!=engine&& null!=engine.getSinks()&&
            engine.getSinks().size()>0){
            List<Sinks> sinks = engine.getSinks();
            for (Sinks sink : sinks){
                //创建表
                executeCreateSinkTables(sink.getCreateTables());
                //执行insert语句
                executeInsertSinkTables(bsTableEnv,sink.getInsertSQLs());
            }
        }

    }

    private static void executeCreateSinkTables(List<String> createSinkTables){
        if (createSinkTables!=null&&createSinkTables.size()>0){
            createSinkTables.forEach(sql->{
                // TODO: 2021/3/23 建表语句实现
            });
        }

    }

    private static void executeInsertSinkTables(StreamTableEnvironment bsTableEnv,List<String> insertSQLs){
        if (insertSQLs!=null&&insertSQLs.size()>0){
            insertSQLs.forEach(sql->{
                bsTableEnv.executeSql(sql).print();
            });
        }

    }


}

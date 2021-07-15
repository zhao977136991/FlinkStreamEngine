package com.shandows.StreamEngine.config;

import com.yx.utils.ScanningFile;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.List;

/**
 * 自定义函数固定放置在com.ai.core.udf包下
 */
public class AutoRegisterUDF {
    private static String packagePath = "com.ai.core.udf";


    public static void autoRegisterUDF(StreamTableEnvironment bsTableEnv){
        ScanningFile scanningFile = new ScanningFile(packagePath);
        List<Class> classList = scanningFile.addClass();
        classList.forEach(aClass -> {
            String className = aClass.getName();
            bsTableEnv.createTemporarySystemFunction(className,aClass);
        });
    }
}

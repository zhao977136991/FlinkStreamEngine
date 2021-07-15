package com.shandows.StreamEngine.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描包下路径
 * 包括本地文件和jar包文件
 * @author ljb
 *
 */
public class ScanningFile {

    private Class<?> superStrategy = String.class;//接口类class 用于过滤 可以不要

    private List<Class<? extends String>> eleStrategyList = new ArrayList<Class<? extends String>>();

    private ClassLoader classLoader = ScanningFile.class.getClassLoader();//默认使用的类加载器

    private  String STARATEGY_PATH ;//需要扫描的策略包名

    public ScanningFile(String STARATEGY_PATH) {
        this.STARATEGY_PATH = STARATEGY_PATH;
    }



    /**
     * 获取包下所有实现了superStrategy的类并加入list
     */
    public List<Class> addClass(){
        URL url = classLoader.getResource(this.STARATEGY_PATH.replace(".", "/"));
        String protocol = url.getProtocol();
        if ("file".equals(protocol)) {
            // 本地自己可见的代码
            return findClassLocal(this.STARATEGY_PATH);
        } else if ("jar".equals(protocol)) {
            // 引用jar包的代码
            findClassJar(this.STARATEGY_PATH);
        }
        return null;
    }

    /**
     * 本地查找
     * @param packName
     */
    private List<Class> findClassLocal(final String packName){
        List<Class> classList = new ArrayList<>();
        URI url = null ;
        try {
            url = classLoader.getResource(packName.replace(".", "/")).toURI();
        } catch (URISyntaxException e1) {
            throw new RuntimeException("未找到策略资源");
        }

        File file = new File(url);
        file.listFiles(new FileFilter() {

            public boolean accept(File chiFile) {
                if(chiFile.isDirectory()){
                    findClassLocal(packName+"."+chiFile.getName());
                }
                if(chiFile.getName().endsWith(".class")){
                    Class<?> clazz = null;
                    try {
                        clazz = classLoader.loadClass(packName + "." + chiFile.getName().replace(".class", ""));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    classList.add(clazz);
                    if(superStrategy.isAssignableFrom(clazz)){
                        eleStrategyList.add((Class<? extends String>) clazz);
                    }
                    return true;
                }
                return false;
            }
        });
        return classList;
    }

    /**
     * jar包查找
     * @param packName
     */
    private void findClassJar(final String packName){
        String pathName = packName.replace(".", "/");
        JarFile jarFile  = null;
        try {
            URL url = classLoader.getResource(pathName);
            JarURLConnection jarURLConnection  = (JarURLConnection )url.openConnection();
            jarFile = jarURLConnection.getJarFile();
        } catch (IOException e) {
            throw new RuntimeException("未找到策略资源");
        }

        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();

            if(jarEntryName.contains(pathName) && !jarEntryName.equals(pathName+"/")){
                //递归遍历子目录
                if(jarEntry.isDirectory()){
                    String clazzName = jarEntry.getName().replace("/", ".");
                    int endIndex = clazzName.lastIndexOf(".");
                    String prefix = null;
                    if (endIndex > 0) {
                        prefix = clazzName.substring(0, endIndex);
                    }
                    findClassJar(prefix);
                }
                if(jarEntry.getName().endsWith(".class")){
                    Class<?> clazz = null;
                    try {
                        clazz = classLoader.loadClass(jarEntry.getName().replace("/", ".").replace(".class", ""));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if(superStrategy.isAssignableFrom(clazz)){
                        eleStrategyList.add((Class<? extends String>) clazz);
                    }
                }
            }

        }

    }
    public static void main(String[] args) {
        ScanningFile s = new ScanningFile("com.ai.core.udf");
        List<Class> classList = s.addClass();
        classList.forEach(aClass -> {
            System.out.println(aClass);
        });
    }

}
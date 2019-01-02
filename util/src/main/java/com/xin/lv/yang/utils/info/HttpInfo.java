package com.xin.lv.yang.utils.info;


import java.io.File;

public class HttpInfo  {
    private String key;
    private String values;
    private File file;


    public  HttpInfo(){}

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public HttpInfo(String key, String values){
        this.key=key;
        this.values=values;
    }
    public HttpInfo(String key, File file){
        this.key=key;
        this.file=file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}

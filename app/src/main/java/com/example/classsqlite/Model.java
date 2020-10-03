package com.example.classsqlite;

public class Model {

    private String id;
    private String name;
    private String code;
    private String type;

    public Model(String id, String name, String code, String type) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }
}

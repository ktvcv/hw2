package com.ithillel.service;

public class Bean {
    private String name;
    private String type;
    private String[] constructorArgs;

    public Bean() {
    }

    public Bean(String name, String type, String[] constructorArgs) {
        this.name = name;
        this.type = type;
        this.constructorArgs = constructorArgs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getConstructorArgs() {
        return constructorArgs;
    }

    public void setConstructorArgs(String[] constructorArgs) {
        this.constructorArgs = constructorArgs;
    }
}

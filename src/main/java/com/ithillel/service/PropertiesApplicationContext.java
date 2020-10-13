package com.ithillel.service;

import com.ithillel.Interfaces.ApplicationContext;
import com.ithillel.Interfaces.Storage;

import java.io.IOException;
import java.util.*;

public class PropertiesApplicationContext implements ApplicationContext {

    private Map<String, Object> beanObjects = new HashMap<>();

    public PropertiesApplicationContext() {
        Properties applicationProperties = new Properties();
        try {
            applicationProperties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 2; i++) {
            buildBean(applicationProperties.getProperty("bean[" + i + "].name"),
                    applicationProperties.getProperty("bean[" + i + "].type"),
                    applicationProperties.getProperty("bean[" + i + "].args"));
        }
    }

    private void buildBean(String name, String type, String args) {
        if (args == null)
            try {
                beanObjects.put(name, Class.forName(type).getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }

        else {
            List<String> argsList = Arrays.asList(args.split(","));
            argsList.forEach(e -> {
                try {
                    if (beanObjects.get(e) != null) {
                        beanObjects.put(name, Class.forName(type).getDeclaredConstructor(Storage.class).newInstance(beanObjects.get(e)));
                        //beanObjects.put(name, Class.forName(type).getConstructor(beanObjects.get(e).getClass().getInterfaces()[0]).newInstance(beanObjects.get(e)));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

        }
    }

    @Override
    public Object getBean(String name) {
        return beanObjects.get(name);
    }

    public static void main(String[] args) {
        PropertiesApplicationContext p = new PropertiesApplicationContext();
        System.out.println("Beans " + p.beanObjects);
    }
}

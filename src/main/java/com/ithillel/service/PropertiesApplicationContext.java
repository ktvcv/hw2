package com.ithillel.service;

import com.ithillel.exceptions.PropertiesLoadException;
import com.ithillel.interfaces.ApplicationContext;
import com.ithillel.interfaces.Storage;

import java.io.IOException;
import java.util.*;

public class PropertiesApplicationContext implements ApplicationContext {

    private Map<String, Object> beanObjects = new HashMap<>();

    public PropertiesApplicationContext() throws IOException {
        Properties applicationProperties = new Properties();
            applicationProperties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));

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
                throw new PropertiesLoadException("Impossible to define class from properties file");
            }

        else {
            List<String> argsList = Arrays.asList(args.split(","));
            argsList.forEach(e -> {
                try {
                    if (beanObjects.get(e) != null) {
                        beanObjects.put(name, Class.forName(type).getDeclaredConstructor(Storage.class).newInstance(beanObjects.get(e)));
                    }
                } catch (Exception ex) {
                    throw new PropertiesLoadException("Impossible to define class from properties file");
                }
            });

        }
    }

    @Override
    public Object getBean(String name) {
        return beanObjects.get(name);
    }

    public static void main(String[] args) throws IOException {
        PropertiesApplicationContext p = new PropertiesApplicationContext();
        System.out.println("Beans " + p.beanObjects);
    }
}

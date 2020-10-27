package com.ithillel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ithillel.service.exceptions.LoadDataRuntimeException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertiesApplicationContext implements ApplicationContext {

    private Map<String, Object> beanObjects;

    public PropertiesApplicationContext() throws IOException {
        this.beanObjects = new HashMap<>();
        this.initBeansConfig();
    }

    public Map<String, Object> getBeanObjects() {
        return beanObjects;
    }

    public void initBeansConfig() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        // convert JSON file to map
        List<Bean> beans = Arrays.asList(mapper.readValue(Paths.get(System.getProperty("pathToTheFile")).toFile(), Bean[].class));

        beans.forEach(bean -> buildBeanObject(bean.getName(), bean.getType(), bean.getConstructorArgs()));

    }

    @Override
    public String getBeans() {
        return beanObjects.toString();
    }

    private void buildBeanObject(String name, String type, String[] args) {
        if (args == null)
            try {
                beanObjects.put(name, Class.forName(type).getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new LoadDataRuntimeException("Impossible to specify data from beans properties file: bean's name " + name + e);

            }
        else {
            List<String> argsList = Arrays.asList(args);
            argsList.forEach(e -> {
                try {
                    if (beanObjects.get(e) != null) {
                        beanObjects.put(name, Class.forName(type).getDeclaredConstructor(Storage.class).newInstance(beanObjects.get(e)));
                    }
                } catch (Exception ex) {
                    throw new LoadDataRuntimeException("Impossible to specify data from beans properties file" + e);

                }
            });

        }
    }


    @Override
    public Object getBean(String name) {
        return beanObjects.get(name);
    }


}

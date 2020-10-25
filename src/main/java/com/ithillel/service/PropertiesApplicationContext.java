package com.ithillel.service;

import com.ithillel.exception.LoadDataRuntimeException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertiesApplicationContext implements ApplicationContext {

    private final Map<String, Object> beanObjects;

    public PropertiesApplicationContext() {
        beanObjects = new HashMap<>();
        initBeans();
    }

    private void initBeans(){
        JSONParser jsonParser = new JSONParser();

        try {
            String FILE_NAME = System.getProperty("pathToTheFile");
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(FILE_NAME));

            JSONArray arrayBean = (JSONArray) obj.get("beans");

            for (Object bean : arrayBean) {
                buildBean((JSONObject) bean);
            }

        } catch (IOException | ParseException e) {
            throw new LoadDataRuntimeException("Impossible to load data from beans json file");
        }
    }

    private void buildBean(JSONObject bean) {
        String name = (String) bean.get("name");
        String type = (String) bean.get("type");

        if(name == null || type == null){
            throw new LoadDataRuntimeException("One ore more non-optional arguments is null");
        }
        JSONArray argsJSON = (JSONArray) bean.get("constructorArgs");

        StringBuilder args = new StringBuilder();
        if (argsJSON != null)
            for (Object o : argsJSON) {
                args.append(o);
            }

        buildBeanObject(name, type, args.toString());
    }

    private void buildBeanObject(String name, String type, String args) {
        if (args.isEmpty())
            try {
                beanObjects.put(name, Class.forName(type).getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                throw new LoadDataRuntimeException("Impossible to define a class from bean " + name);
            }
        else {
            List<String> argsList = Arrays.asList(args.split(","));
            argsList.forEach(e -> {
                try {
                    if (beanObjects.get(e) != null) {
                        beanObjects.put(name, Class.forName(type).getDeclaredConstructor(Storage.class).newInstance(beanObjects.get(e)));
                    }
                } catch (Exception ex) {
                    throw new LoadDataRuntimeException("Impossible to define a class from bean " + name);
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
        System.out.println(p.beanObjects);
    }
}

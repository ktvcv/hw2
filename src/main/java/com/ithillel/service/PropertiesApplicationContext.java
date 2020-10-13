package com.ithillel.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PropertiesApplicationContext implements ApplicationContext {

    private Map<String, Object> beanObjects = new HashMap<>();

    public PropertiesApplicationContext() {
        JSONParser jsonParser = new JSONParser();

        try
        {
            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader("C:\\Users\\Victoria\\IdeaProjects\\TextProcessorHW2.2\\src\\main\\resources\\application.json"));

            JSONArray arrayBean = (JSONArray)obj.get("beans");

            for (Object bean : arrayBean) {
                buildBean((JSONObject) bean);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void buildBean(JSONObject bean)
    {
        String name = (String) bean.get("name");
        String type = (String) bean.get("type");

        JSONArray argsJSON = (JSONArray)bean.get("constructorArgs");

        StringBuilder args = new StringBuilder();
        if(argsJSON != null)
            for (Object o: argsJSON) {
                args.append(o);
        }

        buildBeanObject(name,type,args.toString());
    }

    private void buildBeanObject(String name, String type, String args) {
        if (args.isEmpty())
            try {
                beanObjects.put(name, Class.forName(type).getDeclaredConstructor().newInstance());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        else {
            List<String> argsList = Arrays.asList(args.split(","));
            argsList.forEach(e -> {
                try {
                    if (beanObjects.get(e) != null) {
                        beanObjects.put(name, Class.forName(type).getDeclaredConstructor(Storage.class).newInstance(beanObjects.get(e)));
                        //beanObjects.put(name, Class.forName(type).getConstructor(beanObjects.get(e).getClass().getInterfaces()[?]).newInstance(beanObjects.get(e)));
                    }}
                catch (Exception ex){
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
        System.out.println(p.beanObjects);
    }
}

package com.ithillel.service;

import com.ithillel.exception.AnnotationRuntimeException;
import com.ithillel.interfaces.ApplicationContext;
import com.ithillel.interfaces.CustomBean;
import com.ithillel.interfaces.Storage;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;


import java.util.HashMap;
import java.util.Map;


public class AnnotationApplicationContext implements ApplicationContext {

    private final Map<String, Object> beans;

    public AnnotationApplicationContext() {

        this.beans = new HashMap<>();
        try (ScanResult scanResult = new ClassGraph()
                .whitelistPackages("com.ithillel.service")
                .enableClassInfo()
                .enableAnnotationInfo()
                .scan()) {

            scanResult.getClassesWithAnnotation(CustomBean.class.getName())
                    .forEach(c -> {
                        try {
                            String aClass = c.getName();
                            Class clazz = Class.forName(aClass);
                            CustomBean cB = (CustomBean) clazz.getAnnotation(CustomBean.class);
                            if (cB.args().isEmpty())
                                beans.put(cB.name(), Class.forName(aClass).getDeclaredConstructor().newInstance());
                            else
                                beans.put(cB.name(), Class.forName(aClass).getDeclaredConstructor(Storage.class).newInstance(beans.get(cB.args())));

                        } catch (Exception e) {
                            throw new AnnotationRuntimeException("Class cannot be found " + e);
                        }
                    });
        }

    }

    @Override
    public Object getBean(String name) {
        return beans.get(name);
    }

    public static void main(String[] args) {
        AnnotationApplicationContext p = new AnnotationApplicationContext();

    }
}

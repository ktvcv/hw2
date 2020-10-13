package com.ithillel.service;

import com.ithillel.interfaces.ApplicationContext;
import com.ithillel.interfaces.CustomBean;
import com.ithillel.interfaces.Storage;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


public class AnnotationApplicationContext implements ApplicationContext {

    private Map<String, Object> beans = new HashMap<>();

    public AnnotationApplicationContext() {

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
                            e.printStackTrace();
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

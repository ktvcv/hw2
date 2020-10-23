package com.ithillel.service;

public interface ApplicationContext {
    Object getBean(String name);
    void initBeansConfig();
    String getBeans();
}

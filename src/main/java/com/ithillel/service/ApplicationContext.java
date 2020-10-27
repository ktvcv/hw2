package com.ithillel.service;

import java.io.IOException;

public interface ApplicationContext {
    Object getBean(String name);
    void initBeansConfig() throws IOException;
    String getBeans();
}

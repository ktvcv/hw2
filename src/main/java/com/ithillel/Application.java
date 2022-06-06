package com.ithillel;

import com.ithillel.service.ApplicationContext;
import com.ithillel.service.PropertiesApplicationContext;
import com.ithillel.service.watchservice.WatchFileRuntime;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException, InterruptedException {
        ApplicationContext context = new PropertiesApplicationContext();
        WatchFileRuntime.run(context);
    }

}

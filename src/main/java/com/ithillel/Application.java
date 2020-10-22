package com.ithillel;

import com.ithillel.interfaces.ApplicationContext;
import com.ithillel.service.AnnotationApplicationContext;
import com.ithillel.interfaces.TextProcessor;

public class Application {


    private ApplicationContext applicationContext = new AnnotationApplicationContext();
    private TextProcessor textProcessor;

    public Application() {
        textProcessor = (TextProcessor) applicationContext.getBean("textProcessor");
    }

    public void save(String key, final String text) {
        textProcessor.save(key, text);
    }

    public String getByKey(String key) {
       return textProcessor.getByKey(key);
    }

    public static void main(String[] args) {
        Application application = new Application();
        application.save("1.txt", "simple text");
        System.out.println(application.getByKey("1.txt"));
    }

}

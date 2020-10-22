package com.ithillel.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomBean {
    String name(); //Команда за которую будет отвечать функция (например "привет");

    String args() default ""; //Аргументы команды, использоваться будут для вывода списка команд
}

package com.example.jcrdemo.utils;

import org.springframework.context.ApplicationContext;

/**
 * @author gaowenfeng02
 * @desc
 * @date 2020-03-02
 */
public class ApplicationContextHelper  {

    private static ApplicationContext APPLICATION_CONTEXT;

    public static void setApplicationContext(ApplicationContext applicationContext){
        APPLICATION_CONTEXT = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return APPLICATION_CONTEXT;
    }
}

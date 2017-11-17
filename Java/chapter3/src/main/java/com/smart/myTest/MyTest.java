package com.smart.myTest;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

/**
 * Created by jinxiong on 2017/9/16.
 */
public class MyTest {


    public static void main(String[] args) {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {

            Class clazz = classLoader.loadClass("com.smart.myTest.Car");
            Constructor constructor = clazz.getDeclaredConstructor(null);

            Car car = (Car) constructor.newInstance();



        } catch (Exception e) {

        }

    }
}

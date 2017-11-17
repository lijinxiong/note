package com.smart.myTest;


import org.springframework.context.annotation.Bean;

import java.io.Serializable;

/**
 * Created by jinxiong on 2017/9/16.
 */


public class Car implements Serializable{

    public Car() {
     }

    public Car(String name, float price) {
        this.name = name;
        this.price = price;
    }

    private String name;
    private float price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

package com.example.weatherapp;

public class MyProperties {
    private static MyProperties mInstance= null;

    MainData chosenLocation;
    String chosenLocationCityName;

    protected MyProperties(){}

    public static synchronized MyProperties getInstance() {
        if(null == mInstance){
            mInstance = new MyProperties();
        }
        return mInstance;
    }


}
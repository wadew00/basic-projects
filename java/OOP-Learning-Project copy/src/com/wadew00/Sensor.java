package com.wadew00;

public class Sensor extends SecurityDevice{
    int range;
    public void log(){
        System.out.println("Log: " + this);
        System.out.println("Some sensor stuff");
    }
    public String toString(){
        
        return "\nSecurity Device Type: Sensor\n"+super.toString() + "\nSensor Range: " + range ;
    }

}

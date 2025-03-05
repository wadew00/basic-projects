package com.wadew00;


import com.wadew00.Camera.CameraType;
import com.wadew00.SecurityDevice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Camera extends SecurityDevice{

    enum CameraType {PTZ,EPTZ,STATIONARY};
    CameraType cameraType= CameraType.PTZ;
    private String serialNumber;
    private Position position;
    
    public Camera() {
        System.out.println("Camera being created");
        active = true;
        position = new Position(0,0,0);
        cameraType = CameraType.PTZ;
    }
    public Camera(boolean active, CameraType cameraType,String serialNumber) {
        this.active = active;
        this.cameraType = cameraType;
        this.serialNumber= serialNumber;
    }

    public Camera(String serialNumber) {
        setSerialNumber(serialNumber);
    }
    String getSerialNumber() {
        return serialNumber;
    }
    final void setSerialNumber(String s) {
        this.serialNumber = s;
    }



    public static List<Camera> readCameras() {
      
            List<Camera> cameras = new  ArrayList<Camera>();
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get("cameras.txt"));
                for (String line : lines){
                    cameras.add(parseCamera(line));
                }
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
               
            }
            return cameras;
    }

    public static Camera parseCamera(String s) {
        String[] data = s.split(" ");
        Camera camera = new Camera(data[1]);
        camera.setActive(Boolean.parseBoolean(data[0]));
        Position p  = new Position(Integer.parseInt(data[2]),Integer.parseInt(data[3]),Integer.parseInt(data[4]));
        camera.setPosition(p);
        camera.setCameraType(data[5]);
        return camera;

    }

    Position getPosition() {
        return position;
    }
    void setPosition(Position p) {
        position = p;
    }   
    void setPosition(int a, int b, int c) {
        position.pan= a;
        position.tilt= b;
        position.zoom= c;
    }
    void setPosition() {
        reset();
    }



    private void reset() {
        position.pan = 0;
        position.tilt = 0;
        position.zoom = 0;

    }
    CameraType getCameraType() {
        return cameraType;
    }
    Object getCameraType(boolean returnString) {
        if (returnString) {
            return cameraType.toString();
        } else {
            return getCameraType();
        }
        
        
    }
    void setCameraType (CameraType type) {
        cameraType=type;
    }
    void setCameraType (String s) {
        cameraType = CameraType.valueOf(s);
    }

    @Override
    public String toString() {

        return "\nSecurity Device Type: Camera \n" + super.toString() + "\nSerial number: " + serialNumber +"\n"+ position + "\n" + cameraType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj==null) {
            return false;
        }
        if (getClass()!= obj.getClass()){
            return false;
        }
        Camera other = (Camera) obj;

        if (serialNumber== null && other.serialNumber!= null ) {
            return false;
        } else if (!serialNumber.equals(other.serialNumber)) {
            return false;
        }
        return true;
        }
  


        
    }


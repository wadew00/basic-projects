package com.wadew00;

import com.wadew00.Camera.CameraType;
import java.util.ArrayList;
import java.util.List;





public class App {

    public static void main(String[] args)  {
        List<Camera> listCameras = Camera.readCameras();
        for (Camera c : listCameras) {
            System.out.println(c);
            System.out.println("\n\n");
        }
        Camera c = new Camera(true,CameraType.PTZ,"ABC123");
        Sensor s = new Sensor();
        Position p = new Position(10,12,40);
        c.setPosition(p);
        System.out.println(s);
        System.out.println(c);
       
        List<Camera> cameras = new ArrayList<Camera>();
        cameras.add(new Camera("CNA123"));
        Camera c1 =  new Camera("KLM123");
        cameras.add(c1);

        for (Camera camera: cameras) {
            System.out.println(camera);
        }


        Pair<SecurityDevice,SecurityDevice> pair = new Pair<>();
        pair.setX(new Sensor());
        pair.setY(new Camera("CerealNumber"));
        System.out.println(pair.getX());
        System.out.println(pair.getY());
    }
}

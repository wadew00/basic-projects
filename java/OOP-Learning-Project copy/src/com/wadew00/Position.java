package com.wadew00;

public class Position {
    int pan,tilt,zoom;

    

    public Position(int a ,int b, int c) {
        pan= a;
        tilt=b;
        zoom=c;
    }
    @Override
    public String toString() {
        return "Pan: " + pan + "\nTilt: " + tilt + "\nZoom: " + zoom;
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) {
            return true;
        }
        if (obj== null) {
            return false;
        }
        if (getClass()!=obj.getClass()) {
            return false;
        }
        Position other = (Position) obj;
        if (other.pan != pan) {
            return false;
        }
        else if (tilt!= other.tilt) {
            return false;
        }
        else if (zoom!= other.zoom) {
            return false;
        }
        
        return true;

    }
}

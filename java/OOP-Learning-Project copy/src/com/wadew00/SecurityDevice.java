package com.wadew00;

public abstract  class  SecurityDevice {
    protected  String serialNumber;
    protected   boolean active;

    public boolean getActive(){
        return active;
    }
    public void setActive(boolean b) {
        active = b;
    }
    @Override
    public String toString() {
        return "Security Device active: " +active;
    }
}

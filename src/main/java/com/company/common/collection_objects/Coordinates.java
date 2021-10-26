package com.company.common.collection_objects;

import java.io.Serializable;

public class Coordinates implements Serializable {
    public Coordinates() {
        this.y= 0;
        this.x = null;
    }
    public Coordinates(String coordinates) {
        parseString(coordinates);
    }
    public void parseString(String coordinates) {
        setX(coordinates.split(";")[0].trim());
        this.y = Integer.parseInt(coordinates.split(";")[1].trim());}

    private int y;
    public int getY() {
        return y;
    }
    public void setY(Integer y) {
        this.y = y;
    }
    private Long x;
    public Long getX() {
        return x;
    }
    public void setX(String x) {
        try {
            if(x == null || x.isEmpty()){
                this.x = null;
            } else {
                this.x = Long.parseLong(x);
            }
        } catch (NumberFormatException e) {
            this.x = null;
        }
    }
}

package com.company.common.collection_objects;

import java.io.Serializable;

public class Location implements Serializable {
    public Location() {
        this.x = (double) 0;
        this.y = 0L;
        this.z = 0;

    }
    public Location(String location) {
location= location+" ";
        setX(location.split(";")[0].trim().replace(',', '.'));
        setY(location.split(";")[1].trim());
        setZ(location.split(";")[2].trim().replace(',', '.'));

    }

    public Location(Location other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    private Double x;
    public Double getX() {
        return x;
    }
    public void setX(String x) {
        try {
            if(x == null || x.isEmpty()){
                this.x = null;
            } else {
                Double.parseDouble(x);
            }
        } catch (NumberFormatException e) {
            this.x = null;
        }
    }

    private Long y;
    public Long getY() {
        return y;
    }
    public void setY(String y) {
        try {
            if(y == null || y.isEmpty()){
                this.y = null;
            } else {
                this.y = Long.parseLong(y);
            }
        } catch (NumberFormatException e) {
            this.y = null;
        }
    }

    private float z;
    public float getZ() {
        return z;
    }
    public void setZ(String z) {
        try {
            if(z == null || z.isEmpty()){
                this.z = 0;
            } else {
               this.z = Float.parseFloat(z);
            }
        } catch (NumberFormatException e) {
            this.z = 0;
        }
    }

    public static void main(String[] args) {
        Location l1 = new Location();
        Location l2 = new Location(l1);
        l1.x = (double) 1;
        l1.y = (long) 2;
        l1.z = 3;
        System.out.println(l2.x + " " + l2.y + " " + l2.z);

    }
}

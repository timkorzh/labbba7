package com.company.common.collection_objects;

import java.io.Serializable;

public class Person implements Serializable {
    public Person() {
        this.name = "";
        this.passportID = "";
        this.location = new Location();
    }

    public Person(String name, String passportID, Location location) {
        this.name = name;
        this.passportID = passportID;
        this.location = location;
    }

    public Person(String name) {
        this.name = name;
    }

    private String name;
    private String passportID;
    private Location location;

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public static boolean ifPersonExists() { return true; }
}

package com.example.drivncook;

public class Franchisee {
    private String id;
    private String lastName;
    private String firstName;
    private String distance;
    private String address;
    private String city;

    public Franchisee(String id, String lastName, String firstName, String distance, String address, String city) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.distance = distance;
        this.address = address;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}

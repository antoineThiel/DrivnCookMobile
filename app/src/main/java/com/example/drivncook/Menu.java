package com.example.drivncook;

public class Menu {
    private String name;
    private String price;
    private String id;
    private int reduc;

    public Menu(String name, String price, String id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getReduc() {
        return reduc;
    }

    public void setReduc(int reduc) {
        this.reduc = reduc;
    }
}

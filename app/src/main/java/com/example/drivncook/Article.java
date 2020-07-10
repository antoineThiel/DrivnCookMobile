package com.example.drivncook;

public class Article {
    private String name;
    private String price;
    private String unit;
    private String quantity;
    private String id;
    private int reduc;

    public Article(String name, String price, String unit, String quantity, String id) {
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.quantity = quantity;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getReduc() {
        return reduc;
    }

    public void setReduc(int reduc) {
        this.reduc = reduc;
    }
}

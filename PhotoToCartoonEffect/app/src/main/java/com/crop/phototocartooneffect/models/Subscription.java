package com.crop.phototocartooneffect.models;

// Subscription model class
public class Subscription {
    private String title;
    private double price;
    private String description;

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    private String facilities = "";
    // Constructors
    public Subscription() {
    }

    public Subscription(String title, double price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
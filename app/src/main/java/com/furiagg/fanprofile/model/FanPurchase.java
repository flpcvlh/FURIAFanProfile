package com.furiagg.fanprofile.model;

import java.util.Date;

public class FanPurchase {
    public enum PurchaseType {
        MERCHANDISE,
        TICKET,
        SUBSCRIPTION,
        GAME,
        IN_GAME_ITEM,
        OTHER
    }

    private String productName;
    private Date purchaseDate;
    private double price;
    private PurchaseType type;
    private String details;

    public FanPurchase() {
    }

    public FanPurchase(String productName, Date purchaseDate, double price, PurchaseType type) {
        this.productName = productName;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.type = type;
    }

    // Getters e Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public PurchaseType getType() {
        return type;
    }

    public void setType(PurchaseType type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
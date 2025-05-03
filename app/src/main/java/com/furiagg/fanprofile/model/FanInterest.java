package com.furiagg.fanprofile.model;

public class FanInterest {
    public enum Category {
        GAME,
        TEAM,
        PLAYER,
        TOURNAMENT,
        MERCHANDISE,
        CONTENT_CREATOR
    }

    private Category category;
    private String name;
    private int interestLevel; // 1-5
    private String details;

    public FanInterest() {
    }

    public FanInterest(Category category, String name, int interestLevel, String details) {
        this.category = category;
        this.name = name;
        this.interestLevel = interestLevel;
        this.details = details;
    }

    // Getters e Setters
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInterestLevel() {
        return interestLevel;
    }

    public void setInterestLevel(int interestLevel) {
        this.interestLevel = interestLevel;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
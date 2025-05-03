package com.furiagg.fanprofile.model;

public class EsportsProfile {
    public enum Platform {
        STEAM,
        BATTLENET,
        RIOT,
        EPIC,
        FACEIT,
        ESEA,
        GAMERSCLUB,
        OTHER
    }

    private Platform platform;
    private String username;
    private String profileUrl;
    private boolean verified;
    private String gamesFocus;

    public EsportsProfile() {
    }

    public EsportsProfile(Platform platform, String username, String profileUrl, String gamesFocus) {
        this.platform = platform;
        this.username = username;
        this.profileUrl = profileUrl;
        this.verified = false;
        this.gamesFocus = gamesFocus;
    }

    // Getters e Setters
    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getGamesFocus() {
        return gamesFocus;
    }

    public void setGamesFocus(String gamesFocus) {
        this.gamesFocus = gamesFocus;
    }
}
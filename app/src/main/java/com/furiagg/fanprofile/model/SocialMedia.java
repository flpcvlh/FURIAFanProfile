package com.furiagg.fanprofile.model;

public class SocialMedia {
    public enum Platform {
        TWITTER,
        INSTAGRAM,
        FACEBOOK,
        TWITCH,
        YOUTUBE,
        TIKTOK,
        DISCORD,
        OTHER
    }

    private Platform platform;
    private String username;
    private String profileUrl;
    private boolean connected;
    private String accessToken;

    public SocialMedia() {
    }

    public SocialMedia(Platform platform, String username, String profileUrl) {
        this.platform = platform;
        this.username = username;
        this.profileUrl = profileUrl;
        this.connected = false;
    }

    // Novo construtor com quatro parâmetros
    public SocialMedia(Platform platform, String username, String profileUrl, boolean connected) {
        this.platform = platform;
        this.username = username;
        this.profileUrl = profileUrl;
        this.connected = connected;
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

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
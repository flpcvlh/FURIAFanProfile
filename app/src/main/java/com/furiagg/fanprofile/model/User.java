package com.furiagg.fanprofile.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String id;
    private String name;
    private String email;
    private String cpf;
    private String address;
    private String phone;
    private Date birthDate;
    private String photoUrl;
    private String idDocumentUrl;
    private boolean isVerified;
    private List<SocialMedia> socialMediaAccounts;
    private List<EsportsProfile> esportsProfiles;
    private List<FanInterest> interests;
    private List<FanEvent> events;
    private List<FanPurchase> purchases;

    public User() {
        socialMediaAccounts = new ArrayList<>();
        esportsProfiles = new ArrayList<>();
        interests = new ArrayList<>();
        events = new ArrayList<>();
        purchases = new ArrayList<>();
    }

    // Getters e Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getIdDocumentUrl() {
        return idDocumentUrl;
    }

    public void setIdDocumentUrl(String idDocumentUrl) {
        this.idDocumentUrl = idDocumentUrl;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public List<SocialMedia> getSocialMediaAccounts() {
        return socialMediaAccounts;
    }

    public void setSocialMediaAccounts(List<SocialMedia> socialMediaAccounts) {
        this.socialMediaAccounts = socialMediaAccounts;
    }

    public List<EsportsProfile> getEsportsProfiles() {
        return esportsProfiles;
    }

    public void setEsportsProfiles(List<EsportsProfile> esportsProfiles) {
        this.esportsProfiles = esportsProfiles;
    }

    public List<FanInterest> getInterests() {
        return interests;
    }

    public void setInterests(List<FanInterest> interests) {
        this.interests = interests;
    }

    public List<FanEvent> getEvents() {
        return events;
    }

    public void setEvents(List<FanEvent> events) {
        this.events = events;
    }

    public List<FanPurchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<FanPurchase> purchases) {
        this.purchases = purchases;
    }

    public void addSocialMedia(SocialMedia socialMedia) {
        this.socialMediaAccounts.add(socialMedia);
    }

    public void addEsportsProfile(EsportsProfile profile) {
        this.esportsProfiles.add(profile);
    }

    public void addInterest(FanInterest interest) {
        this.interests.add(interest);
    }

    public void addEvent(FanEvent event) {
        this.events.add(event);
    }

    public void addPurchase(FanPurchase purchase) {
        this.purchases.add(purchase);
    }
}

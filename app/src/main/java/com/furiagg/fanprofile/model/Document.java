// Arquivo: app/src/main/java/com/furiagg/fanprofile/model/Document.java
package com.furiagg.fanprofile.model;

import java.util.Date;

public class Document {
    private String id;
    private String name;
    private String documentUrl;
    private String thumbnailUrl;
    private Date dateAdded;
    private boolean verified;

    public Document() {
        this.dateAdded = new Date();
    }

    public Document(String name, String documentUrl, String thumbnailUrl) {
        this.name = name;
        this.documentUrl = documentUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.dateAdded = new Date();
        this.verified = false;
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

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
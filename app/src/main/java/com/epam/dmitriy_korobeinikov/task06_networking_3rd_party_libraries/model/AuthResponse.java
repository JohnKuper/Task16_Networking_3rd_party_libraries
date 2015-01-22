package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;

/**
 * Created by Dmitriy Korobeynikov on 1/22/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {

    @JsonProperty("app")
    public AppInfo appInfo;
    @JsonProperty("created_at")
    public Date createdAt;
    @JsonProperty("id")
    public int id;
    @JsonProperty("note")
    public String note;
    @JsonProperty("note_url")
    public String noteUrl;
    @JsonProperty("scopes")
    public String[] scopes;
    @JsonProperty("token")
    public String token;
    @JsonProperty("updated_at")
    public Date updatedAt;
    @JsonProperty("url")
    public String url;

    private static class AppInfo {
        @JsonProperty("client_id")
        public String clientId;
        @JsonProperty("name")
        public String name;
        @JsonProperty("url")
        public String appUrl;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteUrl() {
        return noteUrl;
    }

    public void setNoteUrl(String noteUrl) {
        this.noteUrl = noteUrl;
    }

    public String[] getScopes() {
        return scopes;
    }

    public void setScopes(String[] scopes) {
        this.scopes = scopes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


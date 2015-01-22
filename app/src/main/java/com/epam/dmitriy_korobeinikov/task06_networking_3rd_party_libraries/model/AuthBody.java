package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Dmitriy Korobeynikov on 1/22/2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthBody {

    @JsonProperty("client_secret")
    public String client_secret;
    @JsonProperty("scopes")
    public String[] scopes;
    @JsonProperty("note")
    public String note;

    public String getClientSecret() {
        return client_secret;
    }

    public void setClientSecret(String clientSecret) {
        this.client_secret = clientSecret;
    }

    public String[] getScopes() {
        return scopes;
    }

    public void setScopes(String[] scopes) {
        this.scopes = scopes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

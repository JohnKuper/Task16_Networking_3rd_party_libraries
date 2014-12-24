package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.parceler.Parcel;

/**
 * Created by Dmitriy_Korobeinikov on 12/15/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
public class Owner {

    private String login;
    private int owner_id;
    private String avatar_url;
    private String type;
    private boolean site_admin;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OwnerData{");
        sb.append("login='").append(login).append('\'');
        sb.append(", id=").append(owner_id);
        sb.append(", avatar_url='").append(avatar_url).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", site_admin=").append(site_admin);
        sb.append('}');
        return sb.toString();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getId() {
        return owner_id;
    }

    public void setId(int id) {
        this.owner_id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSite_admin() {
        return site_admin;
    }

    public void setSite_admin(boolean site_admin) {
        this.site_admin = site_admin;
    }
}

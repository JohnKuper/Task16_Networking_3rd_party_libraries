package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


import org.parceler.Parcel;
import org.parceler.Transient;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.RepositoriesContract.*;

/**
 * Created by Dmitriy Korobeynikov on 12/15/2014.
 * Contains owner data from JSON and describes database table's fields.
 */
@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = OwnerContent.TABLE_NAME)
public class Owner {

    @JsonIgnore
    @DatabaseField(columnName = OwnerContent._ID, generatedId = true)
    public int id;

    @JsonProperty("login")
    @DatabaseField(columnName = OwnerContent.LOGIN)
    public String login;

    @JsonProperty("avatar_url")
    @DatabaseField(columnName = OwnerContent.AVATAR_URL)
    public String avatarUrl;

    @JsonProperty("type")
    @DatabaseField(columnName = OwnerContent.TYPE)
    public String type;

    @Transient
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = OwnerContent.REPOSITORY_ID)
    public Repository repository;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Owner{");
        sb.append("id=").append(id);
        sb.append(", login='").append(login).append('\'');
        sb.append(", avatarUrl='").append(avatarUrl).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", repository=").append(repository);
        sb.append('}');
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getType() {
        return type;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}

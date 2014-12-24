package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.MyContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by Dmitriy_Korobeinikov on 12/15/2014.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
@Contract
@DatabaseTable(tableName = "repositories")
@DefaultContentUri(authority = MyContract.AUTHORITY, path = MyContract.Repository.REPOSITORY_URI_PATH)
@DefaultContentMimeTypeVnd(name = MyContract.MIMETYPE_NAME, type = MyContract.Repository.MIMETYPE_TYPE)
public class Repository {

    @DatabaseField(columnName = MyContract.SearchResult._ID, generatedId = true)
    private int id = 0;

    @JsonProperty("id")
    @DatabaseField(columnName = "repository_id")
    private int repositoryId;
    private String name;
    private String full_name;
    @JsonProperty("private")
    private boolean mPrivate;
    private String description;
    private Date created_at;
    private int stargazers_count;
    private Owner owner;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ItemsData{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", full_name='").append(full_name).append('\'');
        sb.append(", owner=").append(owner);
        sb.append(", mPrivate=").append(mPrivate);
        sb.append(", created_at='").append(created_at).append('\'');
        sb.append(", stargazers_count=").append(stargazers_count);
        sb.append('}');
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public boolean isPrivate() {
        return mPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        mPrivate = aPrivate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public int getStargazers_count() {
        return stargazers_count;
    }

    public void setStargazers_count(int stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

}

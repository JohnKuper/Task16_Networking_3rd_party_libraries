package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.MyContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.parceler.Parcel;
import org.parceler.Transient;

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

    @JsonIgnore
    @DatabaseField(columnName = MyContract.Repository._ID, generatedId = true)
    public int id = 0;

    @JsonProperty("id")
    @DatabaseField(columnName = MyContract.Repository.REPO_ID)
    public int repositoryId;

    @JsonProperty("name")
    @DatabaseField(columnName = MyContract.Repository.NAME)
    public String name;

    @JsonProperty("full_name")
    @DatabaseField(columnName = MyContract.Repository.FULL_NAME)
    public String fullName;

    @JsonProperty("private")
    @DatabaseField(columnName = MyContract.Repository.PRIVATE)
    public boolean mPrivate;

    @JsonProperty("description")
    @DatabaseField(columnName = MyContract.Repository.DESCRIPTION)
    public String description;

    @JsonProperty("created_at")
    @DatabaseField(columnName = MyContract.Repository.CREATED_AT)
    public Date createdAt;

    @JsonProperty("stargazers_count")
    @DatabaseField(columnName = MyContract.Repository.STARGAZERS_COUNT)
    public int stargazersCount;

    @JsonProperty("owner")
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    public Owner owner;

    @Transient
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    public SearchResult result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(int repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isPrivate() {
        return mPrivate;
    }

    public void setpublic(boolean Isprivate) {
        mPrivate = Isprivate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public SearchResult getResult() {
        return result;
    }

    public void setResult(SearchResult result) {
        this.result = result;
    }
}

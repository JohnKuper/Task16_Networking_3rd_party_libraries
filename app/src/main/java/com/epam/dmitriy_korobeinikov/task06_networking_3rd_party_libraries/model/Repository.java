package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.RepositoryContent;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.parceler.Parcel;
import org.parceler.Transient;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Dmitriy_Korobeinikov on 12/15/2014.
 * Contains repository data from JSON and describes database table's fields.
 */

@Parcel
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = RepositoryContent.TABLE_NAME)
public class Repository {

    @JsonIgnore
    @DatabaseField(columnName = RepositoryContent._ID, generatedId = true)
    public int id = 0;

    @JsonProperty("name")
    @DatabaseField(columnName = RepositoryContent.NAME, unique = true)
    public String name;

    @JsonProperty("full_name")
    @DatabaseField(columnName = RepositoryContent.FULL_NAME)
    public String fullName;

    @JsonProperty("description")
    @DatabaseField(columnName = RepositoryContent.DESCRIPTION)
    public String description;

    @JsonProperty("created_at")
    @DatabaseField(columnName = RepositoryContent.CREATED_AT)
    public Date createdAt;

    @JsonProperty("updated_at")
    @DatabaseField(columnName = RepositoryContent.UPDATED_AT)
    public Date updatedAt;

    @JsonProperty("language")
    @DatabaseField(columnName = RepositoryContent.LANGUAGE)
    public String language;

    @JsonProperty("stargazers_count")
    @DatabaseField(columnName = RepositoryContent.STARGAZERS_COUNT)
    public int stargazersCount;

    @JsonProperty("owner")
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, columnName = RepositoryContent.OWNER_ID)
    public Owner owner;

    @ForeignCollectionField(columnName = RepositoryContent.TAGS_ID, maxEagerLevel = 2, eager = true)
    private Collection<Tag> tags;

    @Transient
    @DatabaseField(foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    public SearchResult result;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }
}

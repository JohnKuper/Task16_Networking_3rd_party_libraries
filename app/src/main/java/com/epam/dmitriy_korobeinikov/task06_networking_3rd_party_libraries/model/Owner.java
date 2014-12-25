package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.MyContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.parceler.Parcel;

/**
 * Created by Dmitriy_Korobeinikov on 12/15/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Parcel
@Contract
@DatabaseTable(tableName = MyContract.Owner.TABLE_NAME)
@DefaultContentUri(authority = MyContract.AUTHORITY, path = MyContract.Owner.OWNER_URI_PATH)
@DefaultContentMimeTypeVnd(name = MyContract.MIMETYPE_NAME, type = MyContract.Owner.MIMETYPE_TYPE)
public class Owner {

    @JsonIgnore
    @DatabaseField(columnName = MyContract.Owner._ID, generatedId = true)
    public int id;

    @JsonProperty("login")
    @DatabaseField(columnName = MyContract.Owner.LOGIN)
    public String login;

    @JsonProperty("id")
    @DatabaseField(columnName = MyContract.Owner.OWNER_ID)
    public int ownerId;

    @JsonProperty("avatar_url")
    @DatabaseField(columnName = MyContract.Owner.AVATAR_URL)
    public String avatarUrl;

    @JsonProperty("type")
    @DatabaseField(columnName = MyContract.Owner.TYPE)
    public String type;

    @JsonProperty("site_admin")
    @DatabaseField(columnName = MyContract.Owner.SITE_ADMIN)
    public boolean siteAdmin;

    @DatabaseField(foreign = true, columnName = MyContract.Owner.REPOSITORY_ID)
    public Repository repository;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSiteAdmin() {
        return siteAdmin;
    }

    public void setSiteAdmin(boolean siteAdmin) {
        this.siteAdmin = siteAdmin;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }
}

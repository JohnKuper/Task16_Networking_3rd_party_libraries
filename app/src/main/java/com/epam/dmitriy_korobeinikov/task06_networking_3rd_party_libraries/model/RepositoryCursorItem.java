package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import android.database.Cursor;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesDateUtils;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.RepositoriesContract.*;

import org.parceler.Parcel;

/**
 * Created by Dmitriy Korobeynikov on 12/29/2014.
 * Contains data after query with join from tables: repositories and owners.
 */
@Parcel
public class RepositoryCursorItem {

    //Repository
    public int repositoryId;
    public String name;
    public String fullName;
    public String description;
    public String createdAt;
    public String updateAt;
    public int stargazersCount;
    public String language;
    public String tagsId;

    //Owner
    public String login;
    public String avatarUrl;
    public String type;

    public void parseDataFromCursor(Cursor cursor) {

        repositoryId = cursor.getInt(cursor.getColumnIndex(RepositoryContent.ID_ALIAS));
        language = cursor.getString(cursor.getColumnIndex(RepositoryContent.LANGUAGE));
        name = cursor.getString(cursor.getColumnIndex(RepositoryContent.NAME));
        fullName = cursor.getString(cursor.getColumnIndex(RepositoryContent.FULL_NAME));
        description = cursor.getString(cursor.getColumnIndex(RepositoryContent.DESCRIPTION));
        createdAt = RepositoriesDateUtils.getFormatDateAsString(cursor, RepositoryContent.CREATED_AT, RepositoriesDateUtils.BASE_DATE_FORMAT);
        updateAt = RepositoriesDateUtils.getElapsedDate(cursor, RepositoryContent.UPDATED_AT);
        stargazersCount = cursor.getInt(cursor.getColumnIndex(RepositoryContent.STARGAZERS_COUNT));

        login = cursor.getString(cursor.getColumnIndex(OwnerContent.LOGIN));
        avatarUrl = cursor.getString(cursor.getColumnIndex(OwnerContent.AVATAR_URL));
        type = cursor.getString(cursor.getColumnIndex(OwnerContent.TYPE));
    }

    public int getRepositoryId() {
        return repositoryId;
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


    public String getDescription() {
        return description;
    }


    public String getCreatedAt() {
        return createdAt;
    }


    public String getUpdateAt() {
        return updateAt;
    }


    public int getStargazersCount() {
        return stargazersCount;
    }


    public String getLanguage() {
        return language;
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

    public String getTagsId() {
        return tagsId;
    }
}

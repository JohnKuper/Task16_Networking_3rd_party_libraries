package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import android.database.Cursor;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesDateUtils;

import org.parceler.Parcel;

/**
 * Created by Dmitriy Korobeynikov on 12/29/2014.
 * Contains data after query with join from tables: repositories and owners.
 */
@Parcel
public class RepositoryCursorItem {

    //Repository
    public String name;
    public String fullName;
    public String description;
    public String createdAt;
    public String updateAt;
    public int stargazersCount;
    public String language;

    //Owner
    public String login;
    public String avatarUrl;
    public String type;

    public void parseDataFromCursor(Cursor cursor) {

        language = cursor.getString(cursor.getColumnIndex("language"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        fullName = cursor.getString(cursor.getColumnIndex("full_name"));
        description = cursor.getString(cursor.getColumnIndex("description"));
        createdAt = RepositoriesDateUtils.getFormatDateAsString(cursor, "created_at", "yyyy-MM-dd HH:mm:ss");
        updateAt = RepositoriesDateUtils.getElapsedDate(cursor, "updated_at");
        stargazersCount = cursor.getInt(cursor.getColumnIndex("stargazers_count"));

        login = cursor.getString(cursor.getColumnIndex("login"));
        avatarUrl = cursor.getString(cursor.getColumnIndex("avatar_url"));
        type = cursor.getString(cursor.getColumnIndex("type"));
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


}

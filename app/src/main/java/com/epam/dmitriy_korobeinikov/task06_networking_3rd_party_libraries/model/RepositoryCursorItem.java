package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import android.database.Cursor;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.OwnerContent;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.RepositoryContent;
import com.j256.ormlite.field.DatabaseField;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by Dmitriy_Korobeinikov on 12/29/2014.
 * Contains data after query from tables: repositories and owners with join.
 */
@Parcel
public class RepositoryCursorItem {

    public int repositoryId;
    public String name;
    public String fullName;
    public boolean mPrivate;
    public String description;
    public String createdAt;
    public int stargazersCount;
    public String login;
    public String avatarUrl;
    public String type;
    public boolean siteAdmin;

    public void parseDataFromCursor(Cursor cursor) {
        repositoryId = cursor.getInt(cursor.getColumnIndex("repo_id"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        fullName = cursor.getString(cursor.getColumnIndex("full_name"));
        mPrivate = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("private")));
        description = cursor.getString(cursor.getColumnIndex("description"));
        createdAt = cursor.getString(cursor.getColumnIndex("created_at"));
        stargazersCount = cursor.getInt(cursor.getColumnIndex("stargazers_count"));
        login = cursor.getString(cursor.getColumnIndex("login"));
        avatarUrl = cursor.getString(cursor.getColumnIndex("avatar_url"));
        type = cursor.getString(cursor.getColumnIndex("type"));
        siteAdmin = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("site_admin")));
    }


}

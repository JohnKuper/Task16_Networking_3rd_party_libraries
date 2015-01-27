package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract.*;

/**
 * Created by Dmitriy Korobeynikov on 26.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = IssueContent.TABLE_NAME)
public class Issue {

    @DatabaseField(columnName = IssueContent._ID, generatedId = true)
    public int auto_id;
    @DatabaseField(columnName = IssueContent.ISSUE_ID)
    public String id;
    @DatabaseField(columnName = IssueContent.BODY, uniqueCombo = true)
    public String body;
    @DatabaseField(columnName = IssueContent.STATE)
    public String state;
    @DatabaseField(columnName = IssueContent.TITLE, uniqueCombo = true)
    public String title;
    @DatabaseField(columnName = IssueContent.OWNER_LOGIN)
    public String ownerLogin;
    @DatabaseField(columnName = IssueContent.REPO_NAME)
    public String repositoryName;

    public int getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(int auto_id) {
        this.auto_id = auto_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }
}

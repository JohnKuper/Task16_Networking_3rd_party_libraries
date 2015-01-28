package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.issue;

import android.database.Cursor;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.utils.RepositoriesDateUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import static com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.IssuesContract.*;

/**
 * Created by Dmitriy Korobeynikov on 26.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = IssueContent.TABLE_NAME)
public class Issue {

    @DatabaseField(columnName = IssueContent._ID, generatedId = true)
    public int auto_id;
    @DatabaseField(columnName = IssueContent.ISSUE_NUMBER)
    public String number;
    @DatabaseField(columnName = IssueContent.BODY)
    public String body;
    @DatabaseField(columnName = IssueContent.STATE)
    public String state;
    @DatabaseField(columnName = IssueContent.TITLE)
    public String title;
    @DatabaseField(columnName = IssueContent.OWNER_LOGIN)
    public String ownerLogin;
    @DatabaseField(columnName = IssueContent.REPO_NAME)
    public String repositoryName;
    @DatabaseField(columnName = IssueContent.UPDATED_AT)
    @JsonProperty(value = "updated_at")
    public Date updatedAt;

    public static Issue fromCursor(Cursor cursor) {
        Issue issue = new Issue();
        issue.auto_id = cursor.getInt(cursor.getColumnIndex(IssueContent._ID));
        issue.number = cursor.getString(cursor.getColumnIndex(IssueContent.ISSUE_NUMBER));
        issue.body = cursor.getString(cursor.getColumnIndex(IssueContent.BODY));
        issue.state = cursor.getString(cursor.getColumnIndex(IssueContent.STATE));
        issue.title = cursor.getString(cursor.getColumnIndex(IssueContent.TITLE));
        issue.updatedAt = RepositoriesDateUtils.getFormatDate(cursor, IssueContent.UPDATED_AT, RepositoriesDateUtils.BASE_DATE_FORMAT);
        return issue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Issue{");
        sb.append("auto_id=").append(auto_id);
        sb.append(", number='").append(number).append('\'');
        sb.append(", body='").append(body).append('\'');
        sb.append(", state='").append(state).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", repositoryName='").append(repositoryName).append('\'');
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", ownerLogin='").append(ownerLogin).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public int getAuto_id() {
        return auto_id;
    }

    public void setAuto_id(int auto_id) {
        this.auto_id = auto_id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

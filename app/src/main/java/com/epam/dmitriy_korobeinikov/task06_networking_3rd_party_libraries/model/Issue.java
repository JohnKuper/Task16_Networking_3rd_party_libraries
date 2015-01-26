package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.Date;

/**
 * Created by Dmitriy Korobeynikov on 26.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    public int comments;
    @JsonProperty("created_at")
    public Date createdAt;
    public String state;
    public String title;
    @JsonProperty("updated_at")
    public Date updatedAt;
    public String url;
    public Owner user;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Issue{");
        sb.append("comments=").append(comments);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", state='").append(state).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", url='").append(url).append('\'');
        sb.append(", user=").append(user);
        sb.append('}');
        return sb.toString();
    }
}

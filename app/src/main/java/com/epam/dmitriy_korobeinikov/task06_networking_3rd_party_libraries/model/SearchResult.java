package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content.SearchResultContent;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Dmitriy_Korobeinikov on 12/15/2014.
 * Contains search result data from JSON and describes database table's fields.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = SearchResultContent.TABLE_NAME)
public class SearchResult {

    @DatabaseField(columnName = SearchResultContent._ID, generatedId = true)
    public int id = 0;

    @JsonProperty("total_count")
    @DatabaseField(columnName = SearchResultContent.TOTAL_COUNT)
    public int totalCount;

    @JsonProperty("incomplete_results")
    @DatabaseField(columnName = SearchResultContent.INCOMPLETE_RESULTS)
    public boolean incompleteResults;

    @ForeignCollectionField(eager = true)
    public Collection<Repository> items;

    public Collection<Repository> getItems() {
        return items;
    }

    public ArrayList<Repository> getItemsAsList() {
        ArrayList<Repository> repositories = new ArrayList<>();
        repositories.addAll(items);
        return repositories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public void setItems(Collection<Repository> items) {
        this.items = items;
    }
}

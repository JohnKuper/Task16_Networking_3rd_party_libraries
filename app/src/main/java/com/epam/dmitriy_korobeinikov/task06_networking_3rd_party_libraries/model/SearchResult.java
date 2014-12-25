package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.provider.MyContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.Contract;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dmitriy_Korobeinikov on 12/15/2014.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Contract
@DatabaseTable(tableName = MyContract.SearchResult.TABLE_NAME)
@DefaultContentUri(authority = MyContract.AUTHORITY, path = MyContract.SearchResult.SEARCH_URI_PATH)
@DefaultContentMimeTypeVnd(name = MyContract.MIMETYPE_NAME, type = MyContract.SearchResult.MIMETYPE_TYPE)
public class SearchResult {

    @DatabaseField(columnName = MyContract.SearchResult._ID, generatedId = true)
    public int id = 0;

    @JsonProperty("total_count")
    @DatabaseField(columnName = MyContract.SearchResult.TOTAL_COUNT)
    public int totalCount;

    @JsonProperty("incomplete_results")
    @DatabaseField(columnName = MyContract.SearchResult.INCOMPLETE_RESULTS)
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

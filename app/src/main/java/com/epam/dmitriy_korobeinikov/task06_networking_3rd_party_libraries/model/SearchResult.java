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

/**
 * Created by Dmitriy_Korobeinikov on 12/15/2014.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Contract
@DatabaseTable(tableName = "search_result")
@DefaultContentUri(authority = MyContract.AUTHORITY, path = MyContract.SearchResult.SEARCH_URI_PATH)
@DefaultContentMimeTypeVnd(name = MyContract.MIMETYPE_NAME, type = MyContract.SearchResult.MIMETYPE_TYPE)
public class SearchResult {

    @DatabaseField(columnName = MyContract.SearchResult._ID, generatedId = true)
    private int id = 0;

    @JsonProperty("total_count")
    @DatabaseField(columnName = "total_count")
    private int totalCount;

    @JsonProperty("incomplete_results")
    @DatabaseField(columnName = "incomplete_results")
    private boolean incompleteResults;

    @ForeignCollectionField(eager = false)
    private ArrayList<Repository> items;

    public int getId() {
        return id;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public ArrayList<Repository> getItems() {
        return items;
    }
}

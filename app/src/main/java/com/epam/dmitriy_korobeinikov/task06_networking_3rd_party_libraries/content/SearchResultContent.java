package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.content;

import android.provider.BaseColumns;

/**
 * Created by Dmitriy_Korobeinikov on 12/26/2014.
 * Describes data about SearchResult.class table in database.
 */
public class SearchResultContent implements BaseColumns {

    public static final String TABLE_NAME = "search_results";

    //Columns
    public static final String TOTAL_COUNT = "total_count";
    public static final String INCOMPLETE_RESULTS = "incomplete_results";

}


package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Dmitriy_Korobeinikov on 12/12/2014.
 * REST interface for retrieving data about GitHub repositories.
 */
public interface GitHub {

    @GET("/search/repositories")
    SearchResult getRepos(@Query("q") String keyword,
                  @Query("sort") String stars,
                  @Query("per_page") int resultOnPage);


}

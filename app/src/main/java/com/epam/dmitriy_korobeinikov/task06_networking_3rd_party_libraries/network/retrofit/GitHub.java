package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.AuthBody;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.AuthResponse;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Dmitriy Korobeynikov on 12/12/2014.
 * REST interface for retrieving data about GitHub repositories.
 */
public interface GitHub {

    @GET("/search/repositories?sort=stars")
    SearchResult getRepos(@Query(value = "q", encodeValue = false) String keyword,
                          @Query("per_page") int resultOnPage);

    @PUT("/authorizations/clients/{clientId}")
    AuthResponse authorization(@Path(value = "clientId", encode = false) String clientId, @Body AuthBody authBody, @Header("Authorization") String str);
}

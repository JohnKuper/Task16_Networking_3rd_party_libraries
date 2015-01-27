package com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.network.retrofit;

import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.AuthBody;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.AuthResponse;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.issue.Issue;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.SearchResult;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.issue.IssueCreateRequest;
import com.epam.dmitriy_korobeinikov.task06_networking_3rd_party_libraries.model.issue.IssueEditRequest;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.PATCH;
import retrofit.http.POST;
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

    @GET("/repos/{userName}/{repoName}/issues?state=all")
    List<Issue> getRepoIssues(@Path(value = "userName", encode = false) String userName, @Path(value = "repoName", encode = false) String repoName, @Header("Authorization") String token);

    @POST("/repos/{userName}/{repoName}/issues")
    Issue createIssue(@Path(value = "userName", encode = false) String userName, @Path(value = "repoName", encode = false) String repoName, @Body IssueCreateRequest issueCreateRequest);

    @PATCH("/repos/{userName}/{repoName}/issues/{issueNumber}")
    Issue updateIssue(@Path(value = "userName", encode = false) String userName, @Path(value = "repoName", encode = false) String repoName, @Path(value = "issueNumber") int issueNumber, @Body IssueEditRequest issueEditRequest);
}

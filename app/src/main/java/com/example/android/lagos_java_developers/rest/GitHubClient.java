package com.example.android.lagos_java_developers.rest;

import com.example.android.lagos_java_developers.model.DeveloperProfile;
import com.example.android.lagos_java_developers.model.JSONResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// Request method and URL specified in the annotation
// Callback for the parsed response is the last parameter

//&page=/

public interface GitHubClient {

    @GET("/search/users?q=location:lagos+language:java&")
    Call<JSONResponse>getJsonResponse(@Query("page") int page);

    @GET("https://api.github.com/search/users/{user}")
    Call<DeveloperProfile> getDevelopersProfile(@Path("user") String devName);
}

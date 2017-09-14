package com.example.android.lagos_java_developers.rest;

import com.example.android.lagos_java_developers.model.JSONResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Request method and URL specified in the annotation

public interface GitHubService {

    @GET("/search/users?q=location:lagos+language:java&")
    Call<JSONResponse>getJsonResponse(@Query("page") int page);

}

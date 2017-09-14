package com.example.android.lagos_java_developers.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.lagos_java_developers.model.DeveloperProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 *  Return the information of a DevProfileUtil type that has been built up from parsing a JSON. */

public final class DevProfileUtil {


//     Tag for the log messages


    private static final String LOG_TAG = DevProfileUtil.class.getSimpleName();


//     Helper methods related to requesting and receiving a user data from GitHub.


    public static DeveloperProfile fetchProfilePageInfo(String requestUrl) {
        // Create URL object
        URL url = NetworkCall.createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = NetworkCall.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract and return relevant fields from the JSON response and create an {@link Developers} object
        return extractDevelopersInfo(jsonResponse);
    }


    private static DeveloperProfile extractDevelopersInfo(String earthquakeJSON) {

        if (TextUtils.isEmpty(earthquakeJSON)) {

            Log.v(LOG_TAG, earthquakeJSON);
        }

        //Extracts developers details

        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);


            String developersNamne = baseJsonResponse.getString("name");
            String bio = baseJsonResponse.getString("bio");
            String public_repos = baseJsonResponse.getString("public_repos");
            String followers = baseJsonResponse.getString("followers");
            String following = baseJsonResponse.getString("following");
            String hireable = baseJsonResponse.getString("hireable");
            String location = baseJsonResponse.getString("location");
            String email = baseJsonResponse.getString("email");


             return new DeveloperProfile(developersNamne, bio, public_repos, followers, following, location, email, hireable);


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the developer JSON results", e);
        }
        return null;
    }


}
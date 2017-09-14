package com.example.android.lagos_java_developers.model;

import com.google.gson.annotations.SerializedName;


public class Developer {

    @SerializedName("login")
    private String mDevName;

    @SerializedName("avatar_url")
    private String mDevImgUrl;

    @SerializedName("url")
    private String mDevUrl;

    @SerializedName ("html_url")
    private String mdevHtmlUrl;



    public String getDevName() {

        return mDevName;
    }

    public String getDevImg() {

        return mDevImgUrl;
    }


    public String getDevUrl() {
        return mDevUrl;
    }

    public String getdevHtmlUrl() {

        return mdevHtmlUrl;
    }

}



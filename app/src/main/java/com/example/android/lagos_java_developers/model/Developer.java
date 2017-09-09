package com.example.android.lagos_java_developers.model;

import com.google.gson.annotations.SerializedName;

/**
 * This  class creates the creates an object of {@Link Developer} class with
 * @parammDevName
 * @parammDevImgUrl
 * @parammDevUrl
 * and provide  methods to return each parameter when called from a reference variable of an object instance of  {@Link Developer}
 */
public class Developer {

    @SerializedName("login")
    private String mDevName;

    @SerializedName("avatar_url")
    private String mDevImgUrl;

    @SerializedName("url")
    private String mDevUrl;

    @SerializedName ("html_url")
    private String mdevHtmlUrl;


//    public Developer(String devName, String DevImgUrl, String devUrl, String devHtmlUrl) {
//
//        mDevName = devName;
//        mDevImgUrl = DevImgUrl;
//        mDevUrl = devUrl;
//        mdevHtmlUrl = devHtmlUrl;
//
//    }


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



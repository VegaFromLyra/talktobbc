package com.handy.fragments.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by abalasubramaniam on 12/11/15.
 */
public class Song implements Serializable {
    @SerializedName("title")
    private String mTitle;

    @SerializedName("artist")
    private String mArtist;

    @SerializedName("label")
    private String mLabel;

    @SerializedName("image")
    private String mImageResource;

    @SerializedName("playlist")
    private String mPlaylist;

    public String getTitle() {
        return mTitle;
    }
}

package com.handy.fragments.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by abalasubramaniam on 12/11/15.
 */
public class PlaylistWrapper implements Serializable {

    @SerializedName("playlist")
    private Playlist mPlaylist;

    public Playlist getPlaylist()
    {
        return mPlaylist;
    }
}

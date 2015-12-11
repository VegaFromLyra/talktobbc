package com.handy.fragments.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by abalasubramaniam on 12/10/15.
 */
public class Playlist implements Serializable {

    public ArrayList<Song> getPlaylistA() {
        return mPlaylistA;
    }

    public ArrayList<Song> getPlaylistB() {
        return mPlaylistB;
    }

    @SerializedName("a")
    private ArrayList<Song> mPlaylistA;

    @SerializedName("b")
    private ArrayList<Song> mPlaylistB;


}

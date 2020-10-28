
package com.github.tiniyield.sequences.benchmarks.operations.model.artist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopArtists {

    @SerializedName("artist")
    @Expose
    private List<Artist> artist = null;
    @SerializedName("@attr")
    @Expose
    private TopArtistsQueryData attr;

    public List<Artist> getArtist() {
        return artist;
    }

    public void setArtist(List<Artist> artist) {
        this.artist = artist;
    }

    public TopArtistsQueryData getAttr() {
        return attr;
    }

    public void setAttr(TopArtistsQueryData attr) {
        this.attr = attr;
    }

}

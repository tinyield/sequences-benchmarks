
package com.github.tiniyield.sequences.benchmarks.operations.model.track;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tracks {

    @SerializedName("track")
    @Expose
    private List<Track> track = null;
    @SerializedName("@attr")
    @Expose
    private TopTracksQueryData attr;

    public List<Track> getTrack() {
        return track;
    }

    public void setTrack(List<Track> track) {
        this.track = track;
    }

    public TopTracksQueryData getAttr() {
        return attr;
    }

    public void setAttr(TopTracksQueryData attr) {
        this.attr = attr;
    }

}

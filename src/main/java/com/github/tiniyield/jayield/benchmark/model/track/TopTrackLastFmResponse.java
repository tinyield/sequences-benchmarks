
package com.github.tiniyield.jayield.benchmark.model.track;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopTrackLastFmResponse {

    @SerializedName("tracks")
    @Expose
    private Tracks tracks;

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

}

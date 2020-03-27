
package com.github.tiniyield.sequences.benchmarks.model.artist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopArtistsLastFmResponse {

    @SerializedName("topartists")
    @Expose
    private TopArtists topartists;

    public TopArtists getTopartists() {
        return topartists;
    }

    public void setTopartists(TopArtists topartists) {
        this.topartists = topartists;
    }

}

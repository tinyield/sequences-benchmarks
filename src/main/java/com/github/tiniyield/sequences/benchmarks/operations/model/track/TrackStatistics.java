
package com.github.tiniyield.sequences.benchmarks.operations.model.track;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TrackStatistics {

    @SerializedName("rank")
    @Expose
    private String rank;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

}

package com.github.tiniyield.sequences.benchmarks.operations.model.track;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "TrackStatistics{" +
                "rank='" + rank + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackStatistics)) return false;
        TrackStatistics that = (TrackStatistics) o;
        return Objects.equals(rank, that.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank);
    }
}

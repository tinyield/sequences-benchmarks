package com.github.tiniyield.sequences.benchmarks.common.model.track;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Streamable {

    @SerializedName("#text")
    @Expose
    private String text;
    @SerializedName("fulltrack")
    @Expose
    private String fulltrack;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFulltrack() {
        return fulltrack;
    }

    public void setFulltrack(String fulltrack) {
        this.fulltrack = fulltrack;
    }

    @Override
    public String toString() {
        return "Streamable{" +
                "text='" + text + '\'' +
                ", fulltrack='" + fulltrack + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Streamable)) return false;
        Streamable that = (Streamable) o;
        return Objects.equals(text, that.text) &&
                Objects.equals(fulltrack, that.fulltrack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, fulltrack);
    }
}

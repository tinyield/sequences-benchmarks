
package com.github.tiniyield.sequences.benchmarks.operations.model.artist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Artist {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("listeners")
    @Expose
    private String listeners;
    @SerializedName("mbid")
    @Expose
    private String mbid;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("streamable")
    @Expose
    private String streamable;
    @SerializedName("image")
    @Expose
    private List<Image> image = null;


    public String getName() {
        return name;
    }

    public Artist setName(String name) {
        this.name = name;
        return this;
    }

    public String getListeners() {
        return listeners;
    }

    public Artist setListeners(String listeners) {
        this.listeners = listeners;
        return this;
    }

    public String getMbid() {
        return mbid;
    }

    public Artist setMbid(String mbid) {
        this.mbid = mbid;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Artist setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getStreamable() {
        return streamable;
    }

    public Artist setStreamable(String streamable) {
        this.streamable = streamable;
        return this;
    }

    public List<Image> getImage() {
        return image;
    }

    public Artist setImage(List<Image> image) {
        this.image = image;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Artist artist = (Artist) o;
        return Objects.equals(name, artist.name) &&
                Objects.equals(listeners, artist.listeners) &&
                Objects.equals(mbid, artist.mbid) &&
                Objects.equals(url, artist.url) &&
                Objects.equals(streamable, artist.streamable) &&
                Objects.equals(image, artist.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, listeners, mbid, url, streamable, image);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                ", listeners='" + listeners + '\'' +
                ", mbid='" + mbid + '\'' +
                ", url='" + url + '\'' +
                ", streamable='" + streamable + '\'' +
                ", image=" + image +
                '}';
    }
}

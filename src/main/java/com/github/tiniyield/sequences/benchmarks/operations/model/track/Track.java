
package com.github.tiniyield.sequences.benchmarks.operations.model.track;

import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Track {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("duration")
    @Expose
    private String duration;
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
    private Streamable streamable;
    @SerializedName("artist")
    @Expose
    private Artist artist;
    @SerializedName("image")
    @Expose
    private List<Image> image = null;
    @SerializedName("@attr")
    @Expose
    private TrackStatistics attr;

    public String getName() {
        return name;
    }

    public Track setName(String name) {
        this.name = name;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public Track setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getListeners() {
        return listeners;
    }

    public Track setListeners(String listeners) {
        this.listeners = listeners;
        return this;
    }

    public String getMbid() {
        return mbid;
    }

    public Track setMbid(String mbid) {
        this.mbid = mbid;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Track setUrl(String url) {
        this.url = url;
        return this;
    }

    public Streamable getStreamable() {
        return streamable;
    }

    public Track setStreamable(Streamable streamable) {
        this.streamable = streamable;
        return this;
    }

    public Artist getArtist() {
        return artist;
    }

    public Track setArtist(Artist artist) {
        this.artist = artist;
        return this;
    }

    public List<Image> getImage() {
        return image;
    }

    public Track setImage(List<Image> image) {
        this.image = image;
        return this;
    }

    public TrackStatistics getAttr() {
        return attr;
    }

    public Track setAttr(TrackStatistics attr) {
        this.attr = attr;
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
        Track track = (Track) o;
        return Objects.equals(name, track.name) &&
                Objects.equals(duration, track.duration) &&
                Objects.equals(listeners, track.listeners) &&
                Objects.equals(mbid, track.mbid) &&
                Objects.equals(url, track.url) &&
                Objects.equals(streamable, track.streamable) &&
                Objects.equals(artist, track.artist) &&
                Objects.equals(image, track.image) &&
                Objects.equals(attr, track.attr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, duration, listeners, mbid, url, streamable, artist, image, attr);
    }

    @Override
    public String toString() {
        return "Track{" +
                "name='" + name + '\'' +
                ", duration='" + duration + '\'' +
                ", listeners='" + listeners + '\'' +
                ", mbid='" + mbid + '\'' +
                ", url='" + url + '\'' +
                ", streamable=" + streamable +
                ", artist=" + artist +
                ", image=" + image +
                ", attr=" + attr +
                '}';
    }
}

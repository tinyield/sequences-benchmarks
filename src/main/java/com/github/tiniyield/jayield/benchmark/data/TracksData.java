package com.github.tiniyield.jayield.benchmark.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jayield.Query;
import org.jooq.lambda.Seq;

import com.github.tiniyield.jayield.benchmark.data.loader.FileLoader;
import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.track.Track;
import com.google.common.collect.FluentIterable;

import one.util.streamex.StreamEx;

public class TracksData implements ICountryBasedDataProvider<Track> {
    private final Map<String, Track[]> data;

    public TracksData(CountriesData countries) {
        data = new HashMap<>();
        countries.asStream().forEach(country -> {
            String name = country.getName();
            data.put(name, new FileLoader().loadTracks(name));
        });
    }

    @Override
    public List<Track> asList(String country) {
        return Arrays.asList(getTracksForCountry(country));
    }

    @Override
    public Stream<Track> asStream(String country) {
        return Stream.of(getTracksForCountry(country));
    }

    @Override
    public StreamEx<Track> asStreamEx(String country) {
        return StreamEx.of(getTracksForCountry(country));
    }

    @Override
    public Query<Track> asQuery(String country) {
        return Query.of(getTracksForCountry(country));
    }

    @Override
    public FluentIterable<Track> asFluentIterable(String country) {
        return FluentIterable.from(getTracksForCountry(country));
    }

    @Override
    public Seq<Track> asSeq(String country) {
        return Seq.of(getTracksForCountry(country));
    }

    private Track[] getTracksForCountry(String country) {
        return this.data.computeIfAbsent(country, (name) -> new Track[0]);
    }

    public boolean hasDataForCountry(String country) {
        return data.containsKey(country) && data.get(country).length > 0;
    }
}

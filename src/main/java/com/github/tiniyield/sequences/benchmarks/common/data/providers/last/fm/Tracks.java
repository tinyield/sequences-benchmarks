package com.github.tiniyield.sequences.benchmarks.common.data.providers.last.fm;

import com.github.tiniyield.sequences.benchmarks.common.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.common.data.providers.rest.countries.Countries;
import com.github.tiniyield.sequences.benchmarks.common.model.track.Track;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Tracks {
    public final Map<String, Track[]> data;

    public Tracks(Countries countries) {
        data = new HashMap<>();
        Arrays.stream(countries.data).forEach(country -> {
            String name = country.getName();
            Track[] countryTracks = new Track[0];
            try {
                countryTracks = new FileLoader().loadTracks(name);
            } catch (RuntimeException e) {
                System.out.println(String.format("data for country %s was ignored.", name));
            }
            data.put(name, countryTracks);
        });
    }

}

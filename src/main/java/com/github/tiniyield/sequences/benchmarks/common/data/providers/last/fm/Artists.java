package com.github.tiniyield.sequences.benchmarks.common.data.providers.last.fm;

import com.github.tiniyield.sequences.benchmarks.common.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.common.data.providers.rest.countries.Countries;
import com.github.tiniyield.sequences.benchmarks.common.model.artist.Artist;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Artists {
    public final Map<String, Artist[]> data;

    public Artists(Countries countries) {
        data = new HashMap<>();
        Arrays.stream(countries.data).forEach(country -> {
            String name = country.getName();
            Artist[] countryArtists = new Artist[0];
            try {
                countryArtists = new FileLoader().loadArtists(name);
            } catch (RuntimeException e) {
                System.out.printf("data for country %s was ignored.%n", name);
            }
            data.put(name, countryArtists);
        });
    }
}

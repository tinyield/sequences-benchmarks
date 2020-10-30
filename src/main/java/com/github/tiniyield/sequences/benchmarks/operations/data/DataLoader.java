package com.github.tiniyield.sequences.benchmarks.operations.data;

import com.github.tiniyield.sequences.benchmarks.operations.data.loader.ArtistsLoader;
import com.github.tiniyield.sequences.benchmarks.operations.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.operations.data.loader.TracksLoader;
import com.github.tiniyield.sequences.benchmarks.operations.model.ApiKey;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;

import java.util.stream.Stream;


public class DataLoader {

    public static void main(String[] args) {
        loadFromApi();
    }

    public static void loadFromApi() {
        ApiKey apiKey = new FileLoader().loadKey();
        ArtistsLoader artistsLoader = new ArtistsLoader();
        TracksLoader trackLoader = new TracksLoader();
        loadCountries().forEach(country -> {
            try {
                artistsLoader.fetch(apiKey, country);
                trackLoader.fetch(apiKey, country);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static Stream<String> loadCountries() {
        return new FileLoader().loadCountries().map(Country::getName);
    }

}

package com.github.tiniyield.sequences.benchmarks.data;

import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.SILENT;
import static java.lang.String.format;

import java.util.stream.Stream;

import org.openjdk.jmh.runner.RunnerException;

import com.github.tiniyield.sequences.benchmarks.data.loader.ArtistsLoader;
import com.github.tiniyield.sequences.benchmarks.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.data.loader.TracksLoader;
import com.github.tiniyield.sequences.benchmarks.model.ApiKey;
import com.github.tiniyield.sequences.benchmarks.model.country.Country;



public class DataLoader {

    public static void main(String[] args) throws RunnerException {
        loadFromApi();
    }

    private static void loadFromApi() {
        ApiKey apiKey = new FileLoader().loadKey();
        ArtistsLoader artistsLoader = new ArtistsLoader();
        TracksLoader trackLoader = new TracksLoader();
        loadCountries().forEach(country -> {
            if (!SILENT) {
                System.out.println(format("Loading for %s", country));
            }
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

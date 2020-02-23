package com.github.tiniyield.jayield.benchmark;

import static java.lang.String.format;

import java.util.stream.Stream;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.github.tiniyield.jayield.benchmark.data.loader.ArtistsLoader;
import com.github.tiniyield.jayield.benchmark.data.loader.FileLoader;
import com.github.tiniyield.jayield.benchmark.data.loader.TracksLoader;
import com.github.tiniyield.jayield.benchmark.model.ApiKey;
import com.github.tiniyield.jayield.benchmark.model.country.Country;


public class BenchmarkEntryPoint {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ZipTopArtistAndTrackByCountryBenchmark.class.getSimpleName())
                .include(ArtistsInTopTenWithTopTenTracksByCountryBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
//        loadFromApi();
    }

    private static void loadFromApi() {
        ApiKey apiKey = new FileLoader().loadKey();
        ArtistsLoader artistsLoader = new ArtistsLoader();
        TracksLoader trackLoader = new TracksLoader();
        loadCountries().forEach(country -> {
            System.out.println(format("Loading for %s", country));
            try{
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

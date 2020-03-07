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
import com.google.common.collect.Streams;


public class BenchmarkEntryPoint {

    private static final int DEFAULT_MEASUREMENT_ITERATIONS = 5;
    private static final int DEFAULT_WARMUP_ITERATIONS = 5;
    private static final int DEFAULT_FORKS = 1;
    private static final String MEASUREMENT_ITERATIONS_FLAG = "-measurementIterations=";
    private static final String WARMUP_ITERATIONS_FLAG = "-warmupIterations=";
    private static final String FORKS_FLAG = "-forks=";
    private static final String SEPARATOR = "=";

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ZipTopArtistAndTrackByCountryBenchmark.class.getSimpleName())
                .include(ArtistsInTopTenWithTopTenTracksByCountryBenchmark.class.getSimpleName())
                .measurementIterations(getMeasurementIterations(args))
                .warmupIterations(getWarmupIterations(args))
                .jvmArgs(getJvmArgs(args))
                .forks(getForks(args))
                .build();
        new Runner(opt).run();
//        loadFromApi();
    }

    private static int getMeasurementIterations(String[] args) {
        return Stream.of(args)
                     .filter(flag -> flag.startsWith(MEASUREMENT_ITERATIONS_FLAG))
                     .findFirst()
                     .map(flag -> flag.split(SEPARATOR)[1])
                     .map(Integer::valueOf)
                     .orElse(DEFAULT_MEASUREMENT_ITERATIONS);
    }

    private static String[] getJvmArgs(String[] args) {
        return new String[]{"-Xms2G", "-Xmx2G"};
    }

    private static int getForks(String[] args) {
        return Stream.of(args)
                     .filter(flag -> flag.startsWith(FORKS_FLAG))
                     .findFirst()
                     .map(flag -> flag.split(SEPARATOR)[1])
                     .map(Integer::valueOf)
                     .orElse(DEFAULT_FORKS);
    }

    private static int getWarmupIterations(String[] args) {
        return Stream.of(args)
                     .filter(flag -> flag.startsWith(WARMUP_ITERATIONS_FLAG))
                     .findFirst()
                     .map(flag -> flag.split(SEPARATOR)[1])
                     .map(Integer::valueOf)
                     .orElse(DEFAULT_WARMUP_ITERATIONS);
    }

    private static void loadFromApi() {
        ApiKey apiKey = new FileLoader().loadKey();
        ArtistsLoader artistsLoader = new ArtistsLoader();
        TracksLoader trackLoader = new TracksLoader();
        loadCountries().forEach(country -> {
            System.out.println(format("Loading for %s", country));
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

    private static void articlesExamples() {
        Stream<String> songs = Stream.of("505", "Amsterdam", "Mural");
        Stream<String> artists = Stream.of("Arctic Monkeys", "Nothing But Thieves", "Lupe Fiasco");

        Streams.zip(songs, artists, (song, artist) -> format("%s by %s", song, artist)).forEach(System.out::println);
    }
}

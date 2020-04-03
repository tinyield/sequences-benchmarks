package com.github.tiniyield.sequences.benchmarks.operations.data;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.SILENT;
import static java.lang.String.format;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.stream.Stream;

import org.openjdk.jmh.runner.RunnerException;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.github.tiniyield.sequences.benchmarks.every.EveryBenchmark;
import com.github.tiniyield.sequences.benchmarks.every.EveryClassBenchmark;
import com.github.tiniyield.sequences.benchmarks.every.EveryIntegerBenchmark;
import com.github.tiniyield.sequences.benchmarks.every.EveryRandomStringBenchmark;
import com.github.tiniyield.sequences.benchmarks.every.EveryStringBenchmark;
import com.github.tiniyield.sequences.benchmarks.operations.data.loader.ArtistsLoader;
import com.github.tiniyield.sequences.benchmarks.operations.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.operations.data.loader.TracksLoader;
import com.github.tiniyield.sequences.benchmarks.operations.model.ApiKey;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;



public class DataLoader {

    public static void main(String[] args) throws RunnerException, IOException {
//        loadFromApi();
        WorkflowGenerator.generateCollectionWorkflow(EveryRandomStringBenchmark.class.getSimpleName(), "every/random-string");
        WorkflowGenerator.generateCollectionWorkflow(EveryIntegerBenchmark.class.getSimpleName(), "every/integer");
        WorkflowGenerator.generateCollectionWorkflow(EveryClassBenchmark.class.getSimpleName(), "every/class");
        WorkflowGenerator.generateCollectionWorkflow(EveryStringBenchmark.class.getSimpleName(), "every/string");
    }

    public static void loadFromApi() {
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

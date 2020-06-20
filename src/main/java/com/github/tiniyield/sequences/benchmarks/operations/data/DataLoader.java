package com.github.tiniyield.sequences.benchmarks.operations.data;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.SILENT;
import static java.lang.String.format;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import com.github.tiniyield.sequences.benchmarks.every.EveryIntegerBenchmark;
import com.github.tiniyield.sequences.benchmarks.kt.operations.KotlinOperations;
import com.github.tiniyield.sequences.benchmarks.operations.JKotlinOperations;
import com.github.tiniyield.sequences.benchmarks.operations.QueryOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamOperations;
import com.github.tiniyield.sequences.benchmarks.operations.data.loader.ArtistsLoader;
import com.github.tiniyield.sequences.benchmarks.operations.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.operations.data.loader.TracksLoader;
import com.github.tiniyield.sequences.benchmarks.operations.model.ApiKey;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import kotlin.sequences.SequencesKt;
import org.jayield.Query;


public class DataLoader {

    public static void main(String[] args) {
//        loadFromApi();
//        WorkflowGenerator.generateWorkflows();
        manualTest();
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

    private static void manualTest() {
        EveryIntegerBenchmark bench = new EveryIntegerBenchmark();
        QueryOperations query = new QueryOperations();
        KotlinOperations kotlin = new KotlinOperations();
        JKotlinOperations jkotlin = new JKotlinOperations();
        StreamOperations stream = new StreamOperations();
        AtomicInteger qCount = new AtomicInteger();
        AtomicInteger kCount = new AtomicInteger();
        AtomicInteger jkCount = new AtomicInteger();
        AtomicInteger sCount = new AtomicInteger();

        bench.init();

        System.out.println(query.every(Query.fromList(bench.lstA).peek(elem -> {qCount.incrementAndGet();}), Query.fromList(bench.lstB).peek(elem -> {qCount.incrementAndGet();}), Integer::equals));
        System.out.println(kotlin.every(SequencesKt.asSequence(bench.lstA.stream().peek(elem -> {kCount.incrementAndGet();}).iterator()), SequencesKt.asSequence(bench.lstB.stream().peek(elem -> {kCount.incrementAndGet();}).iterator()), Integer::equals));
        System.out.println(jkotlin.every(SequencesKt.asSequence(bench.lstA.stream().peek(elem -> {jkCount.incrementAndGet();}).iterator()), SequencesKt.asSequence(bench.lstB.stream().peek(elem -> {jkCount.incrementAndGet();}).iterator()), Integer::equals));
        System.out.println(stream.every(bench.lstA.stream().peek(elem -> {sCount.incrementAndGet();}), bench.lstB.stream().peek(elem -> {sCount.incrementAndGet();}), Integer::equals));

        System.out.println(qCount);
        System.out.println(kCount);
        System.out.println(jkCount);
        System.out.println(sCount);

    }
}

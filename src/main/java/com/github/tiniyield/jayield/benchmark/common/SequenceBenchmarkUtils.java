package com.github.tiniyield.jayield.benchmark.common;

import static java.lang.String.format;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.tiniyield.jayield.benchmark.query.benchmark.JayieldBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.GuavaBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.StreamBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.StreamFlatmapWithIteratorBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.StreamUtilsBenchmark;

public class SequenceBenchmarkUtils {

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static void assertZipTopArtistAndTrackByCountryBenchmarkValidity() {
        assertSameElements(
                JayieldBenchmark.zipTopArtistAndTrackByCountry().toList(),

                StreamBenchmark.zipTopArtistAndTrackByCountry().collect(Collectors.toList()),

                StreamUtilsBenchmark.zipTopArtistAndTrackByCountry().collect(Collectors.toList()),

                GuavaBenchmark.zipTopArtistAndTrackByCountry().collect(Collectors.toList()),

                StreamFlatmapWithIteratorBenchmark.zipTopArtistAndTrackByCountry().collect(Collectors.toList())
        );
    }

    public static void assertArtistsInTopTenWithTopTenTracksByCountryBenchmarkValidity() {
        assertSameElements(
                JayieldBenchmark.artistsInTopTenWithTopTenTracksByCountry()
                                .toList(),

                StreamBenchmark.artistsInTopTenWithTopTenTracksByCountry()
                               .collect(Collectors.toList()),

                StreamUtilsBenchmark.artistsInTopTenWithTopTenTracksByCountry()
                                    .collect(Collectors.toList()),

                GuavaBenchmark.artistsInTopTenWithTopTenTracksByCountry()
                              .collect(Collectors.toList()),

                StreamFlatmapWithIteratorBenchmark.artistsInTopTenWithTopTenTracksByCountry()
                                                  .collect(Collectors.toList())
        );
    }

    private static <T> void assertSameElements(List<T> jayield,
                                               List<T> stream,
                                               List<T> streamUtils,
                                               List<T> guava,
                                               List<T> streamWithIterator) {
        int size = jayield.size();
        if (size != stream.size() || size != streamUtils.size() || size != guava.size() || size != streamWithIterator.size()) {
            throw new RuntimeException("query results are not the same");
        }

        jayield.forEach(triplet -> {
            if (!stream.contains(triplet)) {
                throw new RuntimeException(format(
                        "query results are not the same, could not find triplet %s in streams results",
                        triplet));
            }
            if (!streamUtils.contains(triplet)) {
                throw new RuntimeException(format(
                        "query results are not the same, could not find triplet %s in StreamUtils results",
                        triplet));
            }
            if (!guava.contains(triplet)) {
                throw new RuntimeException(format(
                        "query results are not the same, could not find triplet %s in guava results",
                        triplet));
            }
            if (!streamWithIterator.contains(triplet)) {
                throw new RuntimeException(format(
                        "query results are not the same, could not find triplet %s in stream with iterator results",
                        triplet));
            }
        });
    }
}

package com.github.tiniyield.jayield.benchmark.common;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.tiniyield.jayield.benchmark.alternative.sequence.fluent.iterable.benchmark.FluentIterableBenchmark;
import com.github.tiniyield.jayield.benchmark.alternative.sequence.jayield.benchmark.QueryBenchmark;
import com.github.tiniyield.jayield.benchmark.alternative.sequence.jool.benchmark.JoolBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.GuavaBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.ProtonpackBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.StreamBenchmark;
import com.github.tiniyield.jayield.benchmark.stream.benchmark.StreamFlatmapWithIteratorBenchmark;
import com.github.tiniyield.jayield.benchmark.alternative.sequence.streamex.benchmark.StreamExBenchmark;

public class SequenceBenchmarkUtils {

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static void assertZipTopArtistAndTrackByCountryBenchmarkValidity() {
        assertSameResults(
                QueryBenchmark.zipTopArtistAndTrackByCountry().toList(),

                StreamBenchmark.zipTopArtistAndTrackByCountry().collect(Collectors.toList()),

                ProtonpackBenchmark.zipTopArtistAndTrackByCountry().collect(Collectors.toList()),

                GuavaBenchmark.zipTopArtistAndTrackByCountry().collect(Collectors.toList()),

                StreamFlatmapWithIteratorBenchmark.zipTopArtistAndTrackByCountry().collect(Collectors.toList()),

                StreamExBenchmark.zipTopArtistAndTrackByCountry().collect(Collectors.toList()),

                FluentIterableBenchmark.zipTopArtistAndTrackByCountry().toList(),

                JoolBenchmark.zipTopArtistAndTrackByCountry().toList()
        );
    }

    public static void assertArtistsInTopTenWithTopTenTracksByCountryBenchmarkValidity() {
        assertSameResults(
                QueryBenchmark.artistsInTopTenWithTopTenTracksByCountry()
                              .toList(),

                StreamBenchmark.artistsInTopTenWithTopTenTracksByCountry()
                               .collect(Collectors.toList()),

                ProtonpackBenchmark.artistsInTopTenWithTopTenTracksByCountry()
                                   .collect(Collectors.toList()),

                GuavaBenchmark.artistsInTopTenWithTopTenTracksByCountry()
                              .collect(Collectors.toList()),

                StreamFlatmapWithIteratorBenchmark.artistsInTopTenWithTopTenTracksByCountry()
                                                  .collect(Collectors.toList()),

                StreamExBenchmark.artistsInTopTenWithTopTenTracksByCountry().collect(Collectors.toList()),

                FluentIterableBenchmark.artistsInTopTenWithTopTenTracksByCountry().toList(),

                JoolBenchmark.artistsInTopTenWithTopTenTracksByCountry().toList()
        );
    }

    private static <T> void assertSameResults(List<T>... results) {
        if (results.length < 1) {
            throw new RuntimeException("Insufficient results");
        }
        List<T> first = results[0];
        List<List<T>> otherResults = Arrays.asList(Arrays.copyOfRange(results, 1, results.length));
        boolean sameResultSizes = otherResults.stream()
                                              .map(List::size)
                                              .allMatch(size -> first.size() == size);
        if (!sameResultSizes) {
            throw new RuntimeException("query results do not have the same size");
        }

        boolean sameElements = otherResults.stream().allMatch(result -> result.containsAll(first));

        if (!sameElements) {
            throw new RuntimeException("query results do not have the same elements");
        }

    }
}

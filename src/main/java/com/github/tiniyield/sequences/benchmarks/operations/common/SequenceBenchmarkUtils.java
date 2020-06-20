package com.github.tiniyield.sequences.benchmarks.operations.common;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.ODD;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import kotlin.sequences.Sequence;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jayield.Query;
import org.jooq.lambda.Seq;

import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.EvenExceptEndSequenceDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.EvenExceptMiddleSequenceDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.EvenExceptStartSequenceDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.EvenSequenceDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.IntegerDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.object.ValueDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;

import one.util.streamex.StreamEx;

public class SequenceBenchmarkUtils {

    private static IntegerDataProvider NUMBERS_DATA_PROVIDER;
    private static ValueDataProvider VALUE_DATA_PROVIDER;
    private static EvenSequenceDataProvider EVEN_DATA_PROVIDER;
    private static EvenExceptStartSequenceDataProvider EVEN_EXCEPT_START_DATA_PROVIDER;
    private static EvenExceptMiddleSequenceDataProvider EVEN_EXCEPT_MIDDLE_DATA_PROVIDER;
    private static EvenExceptEndSequenceDataProvider EVEN_EXCEPT_END_DATA_PROVIDER;

    public static void initEvenExceptEndDataProvider(int size) {
        EVEN_EXCEPT_END_DATA_PROVIDER = new EvenExceptEndSequenceDataProvider(size);
    }

    public static void initEvenExceptMiddleDataProvider(int size) {
        EVEN_EXCEPT_MIDDLE_DATA_PROVIDER = new EvenExceptMiddleSequenceDataProvider(size);
    }

    public static void initEvenExceptStartDataProvider(int size) {
        EVEN_EXCEPT_START_DATA_PROVIDER = new EvenExceptStartSequenceDataProvider(size);
    }

    public static void initEvenDataProvider(int size) {
        EVEN_DATA_PROVIDER = new EvenSequenceDataProvider(size);
    }

    public static void initValueDataProvider(int size) {
        VALUE_DATA_PROVIDER = new ValueDataProvider(size);
    }

    public static void initNumbersDataProvider(int size) {
        NUMBERS_DATA_PROVIDER = new IntegerDataProvider(size);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static void assertZipTopArtistAndTrackByCountryBenchmarkValidity(
            Stream<Triplet<Country, Artist, Track>> guava,
            Seq<Triplet<Country, Artist, Track>> jool,
            Query<Triplet<Country, Artist, Track>> query,
            Stream<Triplet<Country, Artist, Track>> stream,
            Stream<Triplet<Country, Artist, Track>> protonpack,
            Stream<Triplet<Country, Artist, Track>> zipline,
            StreamEx<Triplet<Country, Artist, Track>> streamEx,
            io.vavr.collection.Stream<Triplet<Country, Artist, Track>> vavr,
            Sequence<Triplet<Country, Artist, Track>> kotlin,
            Sequence<Triplet<Country, Artist, Track>> jkotlin) {

        assertSameResults(
                query.toList(),
                stream.collect(Collectors.toList()),
                protonpack.collect(Collectors.toList()),
                guava.collect(Collectors.toList()),
                zipline.collect(Collectors.toList()),
                streamEx.collect(Collectors.toList()),
                jool.toList(),
                vavr.toList().asJava(),
                Lists.newArrayList(kotlin.iterator()),
                Lists.newArrayList(jkotlin.iterator())
        );
    }

    public static <T> void assertSameResults(List<T>... results) {
        if (results.length < 1) {
            throw new RuntimeException("Insufficient results");
        }
        List<T> first = results[0];
        List<List<T>> otherResults = Arrays.asList(Arrays.copyOfRange(results, 1, results.length));
        if (first.size() < 1) {
            throw new RuntimeException("results are empty");
        }
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

    public static void assertArtistsInTopTenWithTopTenTracksByCountryBenchmarkValidity(
            Stream<Pair<Country, List<Artist>>> guava,
            Seq<Pair<Country, List<Artist>>> jool,
            Query<Pair<Country, List<Artist>>> query,
            Stream<Pair<Country, List<Artist>>> stream,
            Stream<Pair<Country, List<Artist>>> protonpack,
            Stream<Pair<Country, List<Artist>>> zipline,
            StreamEx<Pair<Country, List<Artist>>> streamEx,
            io.vavr.collection.Stream<Pair<Country, List<Artist>>> vavr,
            Sequence<Pair<Country, List<Artist>>> kotlin,
            Sequence<Pair<Country, List<Artist>>> jkotlin) {

        assertSameResults(
                query.toList(),
                stream.collect(Collectors.toList()),
                protonpack.collect(Collectors.toList()),
                guava.collect(Collectors.toList()),
                zipline.collect(Collectors.toList()),
                streamEx.collect(Collectors.toList()),
                jool.toList(),
                vavr.toList().asJava(),
                Lists.newArrayList(kotlin.iterator()),
                Lists.newArrayList(jkotlin.iterator())
        );
    }

    public static void assertZipPrimeWithValueValidity(Stream<Pair<Integer, Value>> guava,
                                                       Seq<Pair<Integer, Value>> jool,
                                                       Query<Pair<Integer, Value>> query,
                                                       Stream<Pair<Integer, Value>> stream,
                                                       Stream<Pair<Integer, Value>> protonpack,
                                                       Stream<Pair<Integer, Value>> zipline,
                                                       StreamEx<Pair<Integer, Value>> streamEx,
                                                       io.vavr.collection.Stream<Pair<Integer, Value>> vavr,
                                                       Sequence<Pair<Integer, Value>> kotlin,
                                                       Sequence<Pair<Integer, Value>> jkotlin) {
        assertSameResults(
                query.toList(),
                stream.collect(Collectors.toList()),
                protonpack.collect(Collectors.toList()),
                guava.collect(Collectors.toList()),
                zipline.collect(Collectors.toList()),
                streamEx.collect(Collectors.toList()),
                jool.toList(),
                vavr.toList().asJava(),
                Lists.newArrayList(kotlin.iterator()),
                Lists.newArrayList(jkotlin.iterator())
        );
    }

    public static IntegerDataProvider getNumbersDataProvider() {
        return NUMBERS_DATA_PROVIDER;
    }

    public static ValueDataProvider getValueDataProvider() {
        return VALUE_DATA_PROVIDER;
    }

    public static boolean isPrime(Integer value) {
        if (value <= 1) {
            return false;
        }
        if (value <= 3) {
            return true;
        }
        if (value % 2 == 0) {
            return false;
        }
        int i = 3;
        while (i * i <= value) {
            if (value % i == 0) {
                return false;
            }
            i += 2;
        }
        return true;
    }

    public static boolean isEven(Integer value) {
        return value % 2 == 0;
    }

    public static boolean isOdd(Integer value) {
        return value % 2 != 0;
    }

    public static void assertEveryEvenValidity(boolean stream,
                                               boolean streamEx,
                                               boolean query,
                                               boolean jool,
                                               boolean vavr) {
        if (!(stream == streamEx == query == jool == vavr)) {
            throw new RuntimeException("Results were not as expected");
        }
    }

    public static EvenSequenceDataProvider getEvenDataProvider() {
        return EVEN_DATA_PROVIDER;
    }

    public static EvenExceptStartSequenceDataProvider getEvenExceptStartDataProvider() {
        return EVEN_EXCEPT_START_DATA_PROVIDER;
    }

    public static EvenExceptMiddleSequenceDataProvider getEvenExceptMiddleDataProvider() {
        return EVEN_EXCEPT_MIDDLE_DATA_PROVIDER;
    }

    public static EvenExceptEndSequenceDataProvider getEvenExceptEndDataProvider() {
        return EVEN_EXCEPT_END_DATA_PROVIDER;
    }

    public static void assertFindResult(Integer jool, Integer stream, Integer streamEx, Integer query, Integer vavr) {
        if (!(stream.equals(streamEx) && stream.equals(query) && stream.equals(jool) &&
                stream.equals(vavr) && stream.equals(ODD))) {
            throw new RuntimeException("Results were not odd");
        }
    }
}

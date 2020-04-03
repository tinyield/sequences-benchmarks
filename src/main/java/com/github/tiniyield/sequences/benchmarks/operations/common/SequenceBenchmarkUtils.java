package com.github.tiniyield.sequences.benchmarks.operations.common;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.ODD;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.github.tiniyield.sequences.benchmarks.operations.VavrOperations;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.EvenExceptEndSequenceDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.EvenExceptMiddleSequenceDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.EvenExceptStartSequenceDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.EvenSequenceDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.IntegerDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.object.ValueDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.QueryOperations;
import com.github.tiniyield.sequences.benchmarks.operations.JoolOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamExOperations;
import com.github.tiniyield.sequences.benchmarks.operations.GuavaOperations;
import com.github.tiniyield.sequences.benchmarks.operations.ProtonpackOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamOperations;
import com.github.tiniyield.sequences.benchmarks.operations.ZiplineOperations;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils;

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

    public static void assertZipTopArtistAndTrackByCountryBenchmarkValidity(GuavaOperations guava) {
        assertSameResults(
                QueryOperations.zipTopArtistAndTrackByCountry().toList(),

                StreamOperations.zipTopArtistAndTrackByCountry().collect(Collectors.toList()),

                ProtonpackOperations.zipTopArtistAndTrackByCountry().collect(Collectors.toList()),

                guava.zipTopArtistAndTrackByCountry(
                        SequenceBenchmarkStreamUtils.getArtists(),
                        SequenceBenchmarkStreamUtils.getTracks()
                ).collect(Collectors.toList()),

                ZiplineOperations.zipTopArtistAndTrackByCountry().collect(Collectors.toList()),

                StreamExOperations.zipTopArtistAndTrackByCountry().collect(Collectors.toList()),

                JoolOperations.zipTopArtistAndTrackByCountry().toList(),

                VavrOperations.zipTopArtistAndTrackByCountry().toList().asJava()

        );
    }

    public static void assertArtistsInTopTenWithTopTenTracksByCountryBenchmarkValidity(GuavaOperations guava) {
        assertSameResults(
                QueryOperations.artistsInTopTenWithTopTenTracksByCountry()
                               .toList(),

                StreamOperations.artistsInTopTenWithTopTenTracksByCountry()
                                .collect(Collectors.toList()),

                ProtonpackOperations.artistsInTopTenWithTopTenTracksByCountry()
                                    .collect(Collectors.toList()),

                guava.artistsInTopTenWithTopTenTracksByCountry(
                        SequenceBenchmarkStreamUtils.getArtists(),
                        SequenceBenchmarkStreamUtils.getTracks()
                ).collect(Collectors.toList()),

                ZiplineOperations.artistsInTopTenWithTopTenTracksByCountry()
                                 .collect(Collectors.toList()),

                StreamExOperations.artistsInTopTenWithTopTenTracksByCountry().collect(Collectors.toList()),

                JoolOperations.artistsInTopTenWithTopTenTracksByCountry().toList(),

                VavrOperations.artistsInTopTenWithTopTenTracksByCountry().toList().asJava()
        );
    }

    public static void assertZipPrimeWithValueValidity(GuavaOperations guava) {
        assertSameResults(
                QueryOperations.zipPrimeWithValue()
                               .toList(),

                StreamOperations.zipPrimeWithValue()
                                .collect(Collectors.toList()),

                ProtonpackOperations.zipPrimeWithValue()
                                    .collect(Collectors.toList()),

                guava.zipPrimeWithValue(
                        getNumbersDataProvider().asStream(),
                        getValueDataProvider().asStream()
                ).collect(Collectors.toList()),

                ZiplineOperations.zipPrimeWithValue()
                                 .collect(Collectors.toList()),

                StreamExOperations.zipPrimeWithValue().collect(Collectors.toList()),

                JoolOperations.zipPrimeWithValue().toList()
        );
    }

    private static <T> void assertSameResults(List<T>... results) {
        if (results.length < 1) {
            throw new RuntimeException("Insufficient results");
        }
        List<T> first = results[0];
        List<List<T>> otherResults = Arrays.asList(Arrays.copyOfRange(results, 1, results.length));
        if(first.size() < 1) {
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

    public static IntegerDataProvider getNumbersDataProvider() {
        return NUMBERS_DATA_PROVIDER;
    }

    public static ValueDataProvider getValueDataProvider() {
        return VALUE_DATA_PROVIDER;
    }

    public static boolean isPrime(int value) {
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

    public static Integer doubled(Integer value) {
        return value * 2;
    }

    public static void assertEveryEvenValidity() {
        if (!(StreamOperations.everyEven() == StreamExOperations.everyEven() == QueryOperations.everyEven() ==
                JoolOperations.everyEven() == VavrOperations.everyEven())) {
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

    public static void assertFindResult(AbstractBaseDataProvider<Integer> provider) {
        Integer stream = StreamOperations.findFirst(provider).orElseThrow();
        Integer streamEx = StreamExOperations.findFirst(provider).orElseThrow();
        Integer jayield = QueryOperations.findFirst(provider).orElseThrow();
        Integer jool = JoolOperations.findFirst(provider).orElseThrow();
        Integer vavr = VavrOperations.findFirst(provider).getOrElseThrow(RuntimeException::new);
        if(!(stream.equals(streamEx) && stream.equals(jayield) && stream.equals(jool) &&
                stream.equals(vavr) && stream.equals(ODD))) {
            throw new RuntimeException("Results were not odd");
        }
    }
}

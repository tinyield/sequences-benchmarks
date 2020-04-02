package com.github.tiniyield.sequences.benchmarks.operations;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getNumbersDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getValueDataProvider;
import static com.google.common.collect.Streams.zip;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils;

public class GuavaOperations {

    public static Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        return zip(
                SequenceBenchmarkStreamUtils.getArtists(),
                SequenceBenchmarkStreamUtils.getTracks(),
                SequenceBenchmarkStreamUtils.TO_TOP_BY_COUNTRY_TRIPLET
        ).filter(SequenceBenchmarkUtils.distinctByKey(Triplet::getValue1));
    }

    public static Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        return zip(
                SequenceBenchmarkStreamUtils.getArtists(),
                SequenceBenchmarkStreamUtils.getTracks(),
                SequenceBenchmarkStreamUtils.TO_DATA_TRIPLET_BY_COUNTRY
        ).map(SequenceBenchmarkStreamUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public static Stream<Pair<Integer, Value>> zipPrimeWithValue() {
        return zip(
                getNumbersDataProvider().asStream().filter(SequenceBenchmarkUtils::isPrime),
                getValueDataProvider().asStream(),
                Pair::with
        );
    }

    public static <T,U> Stream<Boolean> every(Stream<T> q1, Stream<U> q2, BiPredicate<T,U> predicate) {
        return zip(q1, q2, predicate::test);
    }

    public static <T> Stream<T> find(Stream<T> q1, Stream<T> q2, BiPredicate<T,T> predicate) {
        return zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null).filter(Objects::nonNull);
    }
}

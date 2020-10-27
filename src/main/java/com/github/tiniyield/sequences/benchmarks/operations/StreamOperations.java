package com.github.tiniyield.sequences.benchmarks.operations;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.distinctByKey;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.TO_TOP_BY_COUNTRY_TRIPLET;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.zip;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;

public class StreamOperations {


    public Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(
            Stream<Pair<Country, Stream<Artist>>> artists,
            Stream<Pair<Country, Stream<Track>>> tracks) {

        return zip(artists, tracks, TO_TOP_BY_COUNTRY_TRIPLET)
                .filter(distinctByKey(Triplet::getValue1));
    }

    public Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(
            Stream<Pair<Country, Stream<Artist>>> artists,
            Stream<Pair<Country, Stream<Track>>> tracks) {

        return zip(artists, tracks, TO_DATA_TRIPLET_BY_COUNTRY)
                .map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public Stream<Pair<Integer, Value>> zipPrimeWithValue(Stream<Integer> numbers, Stream<Value> values) {
        return zip(numbers.filter(SequenceBenchmarkUtils::isPrime), values, Pair::with);
    }

    public Optional<Integer> findFirst(Stream<Integer> numbers) {
        return numbers.filter(SequenceBenchmarkUtils::isOdd).findFirst();
    }

    public <T, U> boolean every(Stream<T> q1, Stream<U> q2, BiPredicate<T, U> predicate) {
        return zip(q1, q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    public <T> T find(Stream<T> q1, Stream<T> q2, BiPredicate<T, T> predicate) {
        return zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public Integer flatMapAndReduce(Stream<Stream<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum).orElseThrow(RuntimeException::new);
    }
}

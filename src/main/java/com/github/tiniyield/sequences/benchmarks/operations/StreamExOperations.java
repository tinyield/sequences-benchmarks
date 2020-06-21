package com.github.tiniyield.sequences.benchmarks.operations;


import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import one.util.streamex.StreamEx;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamExUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamExUtils.TO_TOP_BY_COUNTRY_TRIPLET;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;

public class StreamExOperations {

    public StreamEx<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(
            StreamEx<Pair<Country, StreamEx<Artist>>> artists,
            StreamEx<Pair<Country, StreamEx<Track>>> tracks) {

        return artists.zipWith(tracks).map(TO_TOP_BY_COUNTRY_TRIPLET).distinct(Triplet::getValue1);
    }

    public StreamEx<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(
            StreamEx<Pair<Country, StreamEx<Artist>>> artists,
            StreamEx<Pair<Country, StreamEx<Track>>> tracks) {

        return artists
                .zipWith(tracks)
                .map(TO_DATA_TRIPLET_BY_COUNTRY)
                .map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public StreamEx<Pair<Integer, Value>> zipPrimeWithValue(StreamEx<Integer> numbers, StreamEx<Value> values) {
        return numbers.filter(SequenceBenchmarkUtils::isPrime).zipWith(values, Pair::with);
    }

    public boolean isEveryEven(StreamEx<Integer> numbers) {
        return numbers.allMatch(SequenceBenchmarkUtils::isEven);
    }

    public Optional<Integer> findFirst(StreamEx<Integer> numbers) {
        return numbers.filter(SequenceBenchmarkUtils::isOdd).findFirst();
    }

    public <T, U> boolean every(StreamEx<T> q1, StreamEx<U> q2, BiPredicate<T, U> predicate) {
        return q1.zipWith(q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    public <T> T find(StreamEx<T> q1, StreamEx<T> q2, BiPredicate<T, T> predicate) {
        return q1.zipWith(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public Integer flatMapAndReduce(StreamEx<StreamEx<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum).orElseThrow(RuntimeException::new);
    }
}

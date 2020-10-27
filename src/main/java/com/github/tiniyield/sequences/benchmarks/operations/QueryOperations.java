package com.github.tiniyield.sequences.benchmarks.operations;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.distinctByKey;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkQueryUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkQueryUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkQueryUtils.TO_TOP_BY_COUNTRY_TRIPLET;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jayield.Query;

import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;

public class QueryOperations {

    public Query<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(
            Query<Pair<Country, Query<Artist>>> artists,
            Query<Pair<Country, Query<Track>>> tracks) {

        return artists.zip(tracks, TO_TOP_BY_COUNTRY_TRIPLET).filter(distinctByKey(Triplet::getValue1));
    }

    public Query<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(
            Query<Pair<Country, Query<Artist>>> artists,
            Query<Pair<Country, Query<Track>>> tracks) {
        return artists.zip(tracks, TO_DATA_TRIPLET_BY_COUNTRY)
                      .map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public Query<Pair<Integer, Value>> zipPrimeWithValue(Query<Integer> numbers, Query<Value> values) {
        return numbers.filter(SequenceBenchmarkUtils::isPrime).zip(values, Pair::with);
    }

    public Optional<Integer> findFirst(Query<Integer> numbers) {
        return numbers.filter(SequenceBenchmarkUtils::isOdd).findFirst();
    }

    public <T, U> boolean every(Query<T> q1, Query<U> q2, BiPredicate<T, U> predicate) {
        return q1.zip(q2, predicate::test).allMatch(Boolean.TRUE::equals);
    }

    public <T> T find(Query<T> q1, Query<T> q2, BiPredicate<T, T> predicate) {
        return q1.zip(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                 .filter(Objects::nonNull)
                 .findFirst()
                 .orElse(null);
    }

    public Integer flatMapAndReduce(Query<Query<Integer>> input) {
        return input.flatMap(i -> i).reduce(Integer::sum).orElseThrow(RuntimeException::new);
    }
}

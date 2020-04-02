package com.github.tiniyield.sequences.benchmarks.operations;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.distinctByKey;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getEvenDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getNumbersDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getValueDataProvider;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jayield.Query;

import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkQueryUtils;

import one.util.streamex.StreamEx;

public class QueryOperations {

    public static Query<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        return SequenceBenchmarkQueryUtils.getArtists()
                                          .zip(SequenceBenchmarkQueryUtils.getTracks(),
                                               SequenceBenchmarkQueryUtils.TO_TOP_BY_COUNTRY_TRIPLET)
                                          .filter(distinctByKey(Triplet::getValue1));
    }

    public static Query<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        return SequenceBenchmarkQueryUtils.getArtists()
                                          .zip(SequenceBenchmarkQueryUtils.getTracks(),
                                               SequenceBenchmarkQueryUtils.TO_DATA_TRIPLET_BY_COUNTRY)
                                          .map(SequenceBenchmarkQueryUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public static Query<Pair<Integer, Value>> zipPrimeWithValue() {
        return getNumbersDataProvider().asQuery()
                                       .filter(SequenceBenchmarkUtils::isPrime)
                                       .zip(getValueDataProvider().asQuery(), Pair::with);
    }

    public static boolean everyEven() {
        return getEvenDataProvider().asQuery()
                                    .allMatch(SequenceBenchmarkUtils::isEven);
    }

    public static Optional<Integer> findFirst(AbstractBaseDataProvider<Integer> provider) {
        return provider.asQuery()
                       .filter(SequenceBenchmarkUtils::isOdd)
                       .findFirst();
    }

    public static <T,U> Query<Boolean> every(Query<T> q1, Query<U> q2, BiPredicate<T,U> predicate) {
        return q1.zip(q2, predicate::test);
    }

    public static <T> Query<T> find(Query<T> q1, Query<T> q2, BiPredicate<T,T> predicate) {
        return q1.zip(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null).filter(Objects::nonNull);
    }
}

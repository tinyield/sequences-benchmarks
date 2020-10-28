package com.github.tiniyield.sequences.benchmarks.operations;

import com.github.tiniyield.sequences.benchmarks.kt.operations.utils.SequenceBenchmarkKtSequenceUtils;
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

public class JKotlinOperations {

    public Sequence<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(Sequence<Pair<Country, Sequence<Artist>>> artists,
                                                                                   Sequence<Pair<Country, Sequence<Track>>> tracks) {
        return SequencesKt.distinctBy(
                SequencesKt.map(
                        SequencesKt.zip(artists, tracks),
                        pair -> SequenceBenchmarkKtSequenceUtils.INSTANCE.getTO_TOP_BY_COUNTRY_TRIPLET().apply(pair.getFirst(), pair.getSecond())
                ),
                Triplet::getValue1
        );
    }


    public Sequence<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(Sequence<Pair<Country, Sequence<Artist>>> artists,
                                                                                          Sequence<Pair<Country, Sequence<Track>>> tracks) {
        return SequencesKt.map(
                SequencesKt.map(
                        SequencesKt.zip(artists, tracks),
                        pair -> SequenceBenchmarkKtSequenceUtils.INSTANCE.getTO_DATA_TRIPLET_BY_COUNTRY().apply(pair.getFirst(), pair.getSecond())
                ),
                SequenceBenchmarkKtSequenceUtils.INSTANCE.getTO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY()::apply
        );
    }

    public Sequence<Pair<Integer, Value>> zipPrimeWithValue(Sequence<Integer> numbers, Sequence<Value> values) {
        return SequencesKt.zip(
                SequencesKt.filter(numbers, SequenceBenchmarkUtils::isPrime),
                values,
                Pair::with
        );
    }

    public Optional<Integer> findFirst(Sequence<Integer> numbers) {
        return Optional.ofNullable(SequencesKt.first(SequencesKt.filter(numbers, SequenceBenchmarkUtils::isOdd)));
    }

    public <T, U> boolean every(Sequence<T> q1, Sequence<U> q2, BiPredicate<T, U> predicate) {
        return SequencesKt.all(SequencesKt.zip(q1, q2, predicate::test), Boolean.TRUE::equals);
    }

    public <T> T find(Sequence<T> q1, Sequence<T> q2, BiPredicate<T, T> predicate) {
        return SequencesKt.first(
                SequencesKt.filter(
                        SequencesKt.zip(q1, q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null),
                        Objects::nonNull
                )
        );
    }

}

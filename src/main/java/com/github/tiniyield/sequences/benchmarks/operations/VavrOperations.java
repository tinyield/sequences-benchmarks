package com.github.tiniyield.sequences.benchmarks.operations;


import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkVavrUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkVavrUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkVavrUtils.TO_TOP_BY_COUNTRY_TRIPLET;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;

import io.vavr.collection.Stream;
import io.vavr.control.Option;

public class VavrOperations {

    public Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(
            Stream<Pair<Country, Stream<Artist>>> artists,
            Stream<Pair<Country, Stream<Track>>> tracks) {

        return artists.zip(tracks).map(TO_TOP_BY_COUNTRY_TRIPLET).distinctBy(Triplet::getValue1);
    }

    public Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(
            Stream<Pair<Country, Stream<Artist>>> artists,
            Stream<Pair<Country, Stream<Track>>> tracks) {

        return artists.zip(tracks)
                      .map(TO_DATA_TRIPLET_BY_COUNTRY)
                      .map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public Stream<Pair<Integer, Value>> zipPrimeWithValue(Stream<Integer> numbers, Stream<Value> values) {
        return numbers.filter(SequenceBenchmarkUtils::isPrime).zipWith(values, Pair::with);
    }

    public boolean isEveryEven(Stream<Integer> numbers) {
        return numbers.forAll(SequenceBenchmarkUtils::isEven);
    }

    public Option<Integer> findFirst(Stream<Integer> numbers) {
        return numbers.find(SequenceBenchmarkUtils::isOdd);
    }

    public <T, U> Stream<Boolean> every(Stream<T> q1, Stream<U> q2, BiPredicate<T, U> predicate) {
        return q1.zipWith(q2, predicate::test);
    }

    public <T> Stream<T> find(Stream<T> q1, Stream<T> q2, BiPredicate<T, T> predicate) {
        return q1.zipWith(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null).filter(Objects::nonNull);
    }
}

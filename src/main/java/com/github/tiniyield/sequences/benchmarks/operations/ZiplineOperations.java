package com.github.tiniyield.sequences.benchmarks.operations;

import java.util.Iterator;
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

public class ZiplineOperations {

    public Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(
            Stream<Pair<Country, Stream<Artist>>> artists,
            Stream<Pair<Country, Stream<Track>>> tracks) {

        Iterator<Pair<Country, Stream<Artist>>> iter = artists.iterator();
        return tracks.map(r -> {
            Track track = r.getValue1().findFirst().orElse(null);
            Pair<Country, Stream<Artist>> l = iter.next();
            return Triplet.with(l.getValue0(), l.getValue1().iterator().next(), track);
        }).filter(SequenceBenchmarkUtils.distinctByKey(Triplet::getValue1));
    }

    public Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(
            Stream<Pair<Country, Stream<Artist>>> artists,
            Stream<Pair<Country, Stream<Track>>> tracks) {

        Iterator<Pair<Country, Stream<Artist>>> iter = artists.iterator();
        return tracks.map(r -> {
            Pair<Country, Stream<Artist>> l = iter.next();
            return Triplet.with(l.getValue0(),
                                l.getValue1(),
                                r.getValue1());
        }).map(SequenceBenchmarkStreamUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public Stream<Pair<Integer, Value>> zipPrimeWithValue(Stream<Integer> numbers, Stream<Value> values) {
        Iterator<Value> iter = values.iterator();
        return numbers.filter(SequenceBenchmarkUtils::isPrime).map(v -> Pair.with(v, iter.next()));
    }

    public <T, U> boolean every(Stream<T> q1, Stream<U> q2, BiPredicate<T, U> predicate) {
        Iterator<U> it = q2.iterator();
        return q1.map(t -> predicate.test(t, it.next())).allMatch(Boolean.TRUE::equals);
    }

    public <T> T find(Stream<T> q1, Stream<T> q2, BiPredicate<T, T> predicate) {
        Iterator<T> it = q2.iterator();
        return q1.map(t -> predicate.test(t, it.next()) ? t : null)
                 .filter(Objects::nonNull)
                 .findFirst()
                 .orElse(null);
    }
}

package com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations;

import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.PRIME_NUMBERS_DATA_PROVIDER;
import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.VALUE_DATA_PROVIDER;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.model.wrapper.Value;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkStreamUtils;

public class ZiplineBenchmark {

    public static Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        Iterator<Pair<Country, Stream<Artist>>> iter = SequenceBenchmarkStreamUtils.getArtists().iterator();

        return SequenceBenchmarkStreamUtils.getTracks()
                                           .map(r -> {
                    Track track = r.getValue1().findFirst().orElse(null);
                    Pair<Country, Stream<Artist>> l = iter.next();
                    return Triplet.with(l.getValue0(), l.getValue1().iterator().next(), track);
                })
                                           .filter(SequenceBenchmarkUtils.distinctByKey(Triplet::getValue1));
    }

    public static Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        Iterator<Pair<Country, Stream<Artist>>> iter = SequenceBenchmarkStreamUtils.getArtists().iterator();

        return SequenceBenchmarkStreamUtils.getTracks().map(r -> {
            Pair<Country, Stream<Artist>> l = iter.next();
            return Triplet.with(l.getValue0(),
                                l.getValue1(),
                                r.getValue1());
        })
                                           .map(SequenceBenchmarkStreamUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public static Stream<Pair<Integer, Value>> zipPrimeWithValue() {
        Iterator<Integer> iter = PRIME_NUMBERS_DATA_PROVIDER.asStream().iterator();
        Stream<Value> stream = VALUE_DATA_PROVIDER.asStream();
        return stream.map(v -> Pair.with(iter.next(), v));
    }
}

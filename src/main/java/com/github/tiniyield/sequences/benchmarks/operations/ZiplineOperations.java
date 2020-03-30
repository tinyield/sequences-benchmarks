package com.github.tiniyield.sequences.benchmarks.operations;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getNumbersDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getValueDataProvider;

import java.util.Iterator;
import java.util.List;
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
        Iterator<Value> iter = getValueDataProvider().asStream().iterator();
        Stream<Integer> stream = getNumbersDataProvider().asStream();
        return stream.filter(SequenceBenchmarkUtils::isPrime)
                     .map(v -> Pair.with(v, iter.next()));
    }
}

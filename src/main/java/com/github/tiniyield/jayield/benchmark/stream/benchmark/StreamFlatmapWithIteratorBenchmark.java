package com.github.tiniyield.jayield.benchmark.stream.benchmark;

import static com.github.tiniyield.jayield.benchmark.stream.SequenceBenchmarkStreamUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;
import static com.github.tiniyield.jayield.benchmark.stream.SequenceBenchmarkStreamUtils.getArtists;
import static com.github.tiniyield.jayield.benchmark.stream.SequenceBenchmarkStreamUtils.getTracks;
import static java.util.stream.Stream.of;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkUtils;
import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.track.Track;

public class StreamFlatmapWithIteratorBenchmark {

    public static Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        Iterator<Pair<Country, Stream<Artist>>> iter = getArtists().iterator();

        return getTracks()
                .flatMap(r -> {
                    Track track = r.getValue1().findFirst().orElse(null);
                    return of(iter.next())
                            .map(l -> Triplet.with(l.getValue0(),
                                                   l.getValue1().iterator().next(),
                                                   track));
                })
                .filter(SequenceBenchmarkUtils.distinctByKey(Triplet::getValue1));
    }

    public static Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        Iterator<Pair<Country, Stream<Artist>>> iter = getArtists().iterator();

        return getTracks().flatMap(r -> of(iter.next()).map(l -> Triplet.with(l.getValue0(),
                                                                              l.getValue1(),
                                                                              r.getValue1())))
                          .map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }
}

package com.github.tiniyield.jayield.benchmark.stream.benchmark.zip;

import java.util.Iterator;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkUtils;
import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.track.Track;
import com.github.tiniyield.jayield.benchmark.stream.SequenceBenchmarkStreamUtils;

public class StreamFlatmapWithIteratorBenchmark {

    public static Stream<Triplet<Country, Artist, Track>> query() {
        Iterator<Pair<Country, Stream<Artist>>> iter = SequenceBenchmarkStreamUtils.getArtists().iterator();

        return SequenceBenchmarkStreamUtils.getTracks()
                                           .flatMap(r -> {
                                               Track track = r.getValue1().findFirst().orElse(null);
                                               return Stream.of(iter.next())
                                                            .map(l -> Triplet.with(l.getValue0(),
                                                                                   l.getValue1().iterator().next(),
                                                                                   track));
                                           })
                                           .filter(SequenceBenchmarkUtils.distinctByKey(Triplet::getValue1));
    }
}

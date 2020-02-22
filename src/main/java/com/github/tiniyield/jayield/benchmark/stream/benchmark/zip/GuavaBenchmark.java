package com.github.tiniyield.jayield.benchmark.stream.benchmark.zip;

import static com.google.common.collect.Streams.zip;

import java.util.stream.Stream;

import org.javatuples.Triplet;

import com.github.tiniyield.jayield.benchmark.stream.SequenceBenchmarkStreamUtils;
import com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkUtils;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.track.Track;
import com.google.common.collect.Streams;

import com.github.tiniyield.jayield.benchmark.model.artist.Artist;

public class GuavaBenchmark {

    public static Stream<Triplet<Country, Artist, Track>> query() {
        return zip(
                SequenceBenchmarkStreamUtils.getArtists(),
                SequenceBenchmarkStreamUtils.getTracks(),
                (l, r) -> Triplet.with(
                        l.getValue0(),
                        l.getValue1().findFirst().orElse(null),
                        r.getValue1().findFirst().orElse(null))
        ).filter(SequenceBenchmarkUtils.distinctByKey(Triplet::getValue1));
    }
}

package com.github.tiniyield.jayield.benchmark.query.benchmark.zip;

import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkUtils.distinctByKey;

import org.javatuples.Triplet;
import org.jayield.Query;

import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.track.Track;
import com.github.tiniyield.jayield.benchmark.query.SequenceBenchmarkQueryUtils;

public class JayieldBenchmark {

    public static Query<Triplet<Country, Artist, Track>> query() {
        return SequenceBenchmarkQueryUtils.getArtists()
                                          .zip(SequenceBenchmarkQueryUtils.getTracks(),
                                        (l, r) -> Triplet.with(
                                                l.getValue0(),
                                                l.getValue1().findFirst().orElse(null),
                                                r.getValue1().findFirst().orElse(null)))
                                          .filter(distinctByKey(Triplet::getValue1));
    }
}

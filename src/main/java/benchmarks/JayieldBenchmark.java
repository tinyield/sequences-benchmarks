package benchmarks;

import static benchmarks.SequenceBenchmarkUtils.distinctByKey;

import org.javatuples.Triplet;
import org.jayield.Query;

import model.artist.Artist;
import model.country.Country;
import model.track.Track;

public class JayieldBenchmark {

    static Query<Triplet<Country, Artist, Track>> query() {
        return SequenceBenchmarkQueryUtils.getArtists()
                                   .zip(SequenceBenchmarkQueryUtils.getTracks(),
                                        (l, r) -> Triplet.with(
                                                l.getValue0(),
                                                l.getValue1().findFirst().orElse(null),
                                                r.getValue1().findFirst().orElse(null)))
                                   .filter(distinctByKey(Triplet::getValue1));
    }
}

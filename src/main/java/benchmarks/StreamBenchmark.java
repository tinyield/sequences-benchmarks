package benchmarks;

import static benchmarks.SequenceBenchmarkUtils.distinctByKey;

import java.util.stream.Stream;

import org.javatuples.Triplet;

import model.artist.Artist;
import model.country.Country;
import model.track.Track;

public class StreamBenchmark {

    static Stream<Triplet<Country, Artist, Track>> query() {
        return SequenceBenchmarkStreamUtils.zip(
                SequenceBenchmarkStreamUtils.getArtists(),
                SequenceBenchmarkStreamUtils.getTracks(),
                (l, r) -> Triplet.with(
                        l.getValue0(),
                        l.getValue1().findFirst().orElse(null),
                        r.getValue1().findFirst().orElse(null))
        ).filter(distinctByKey(Triplet::getValue1));
    }
}

package benchmarks;

import static benchmarks.SequenceBenchmarkUtils.distinctByKey;

import java.util.Iterator;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import model.artist.Artist;
import model.country.Country;
import model.track.Track;

public class StreamWithIteratorBenchmark {

    static Stream<Triplet<Country, Artist, Track>> query() {
        Iterator<Pair<Country, Stream<Artist>>> iter = SequenceBenchmarkStreamUtils.getArtists().iterator();

        return SequenceBenchmarkStreamUtils.getTracks()
                                           .flatMap(r -> {
                                               Track track = r.getValue1().findFirst().orElse(null);
                                               return Stream.of(iter.next())
                                                            .map(l -> Triplet.with(l.getValue0(),
                                                                                   l.getValue1().iterator().next(),
                                                                                   track));
                                           })
                                           .filter(distinctByKey(Triplet::getValue1));
    }
}

package benchmarks;

import static benchmarks.SequenceBenchmarkUtils.distinctByKey;
import static benchmarks.SequenceBenchmarkUtils.zip;
import static java.util.stream.Stream.of;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.codepoetics.protonpack.StreamUtils;
import com.google.common.collect.Streams;

import model.artist.Artist;
import model.country.Country;
import model.track.Track;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
public class SequenceBenchmark {

    private SequenceBenchmarkUtils utils;

    @Setup
    public void setup() {
        utils = new SequenceBenchmarkUtils();
    }

    @Benchmark
    public void streamUtils(Blackhole bh) {
        StreamUtils.zip(utils.getArtistsByCountry().stream(), utils.getTracksByCountry().stream(),
                        (l, r) -> Triplet.with(
                                l.getValue0(),
                                l.getValue1().get(0),
                                r.getValue1().get(0)))
                   .filter(distinctByKey(Triplet::getValue1))
                   .forEach(bh::consume);
    }

    @Benchmark
    public void guava(Blackhole bh) {
        Streams.zip(utils.getArtistsByCountry().stream(), utils.getTracksByCountry().stream(),
                    (l, r) -> Triplet.with(
                                l.getValue0(),
                                l.getValue1().get(0),
                                r.getValue1().get(0)))
               .filter(distinctByKey(Triplet::getValue1))
               .forEach(bh::consume);
    }

    @Benchmark
    public void streamWithAuxiliaryFunction(Blackhole bh) {
        zip(
                utils.getArtistsByCountry().stream(),
                utils.getTracksByCountry().stream(),
                (l, r) -> Triplet.with(
                        l.getValue0(),
                        l.getValue1().get(0),
                        r.getValue1().get(0))
        ).forEach(bh::consume);
    }

    @Benchmark
    public void streamWithArtistsIterator(Blackhole bh) {
        Iterator<Pair<Country, List<Artist>>> iter = utils.getArtistsByCountry().iterator();

        utils.getTracksByCountry().stream()
                             .flatMap(r -> of(iter.next()).map(l -> Triplet.with(l.getValue0(),
                                                                    l.getValue1().iterator().next(),
                                                                    r.getValue1().get(0))))
                             .filter(distinctByKey(Triplet::getValue1))
                             .forEach(bh::consume);
    }

    @Benchmark
    public void streamWithTracksIterator(Blackhole bh) {
        Iterator<Pair<Country, List<Track>>> iter = utils.getTracksByCountry().iterator();

        utils.getArtistsByCountry().stream()
             .flatMap(r -> of(iter.next()).map(l -> Triplet.with(l.getValue0(),
                                                                    l.getValue1().iterator().next(),
                                                                    r.getValue1().get(0))))
             .filter(distinctByKey(Triplet::getValue1))
             .forEach(bh::consume);
    }

    @Benchmark
    public void jayield(Blackhole bh) {
        utils.getArtistByCountryAsQuery().zip(utils.getTracksByCountryAsQuery(),
                     (l, r) -> Triplet.with(
                             l.getValue0(),
                             l.getValue1().get(0),
                             r.getValue1().get(0))).traverse(bh::consume);
    }

}

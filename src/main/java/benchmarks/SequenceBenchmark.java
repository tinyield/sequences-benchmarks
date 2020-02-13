package benchmarks;

import static java.util.stream.Stream.of;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jayield.Query;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import com.codepoetics.protonpack.StreamUtils;

import model.artist.Artist;
import model.country.Country;
import model.country.Language;
import model.track.Track;
import query.ArtistsQuery;
import query.CountryQuery;
import query.TracksQuery;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
public class SequenceBenchmark {

    private final String ENGLISH = "en";
    CountryQuery countryQuery;
    ArtistsQuery artistsQuery;
    TracksQuery tracksQuery;
    List<Pair<Country, List<Artist>>> artistsByCountry;
    List<Pair<Country, List<Track>>> tracksByCountry;

    @Setup
    public void setup() {
        countryQuery = new CountryQuery();
        artistsQuery = new ArtistsQuery(countryQuery);
        tracksQuery = new TracksQuery(countryQuery);

        artistsByCountry = countryQuery.getCountries()
                                       .filter(this::isNonEnglishSpeaking)
                                       .map(country -> Pair.with(country,
                                                                 artistsQuery.getArtists(
                                                                         country.getName())))
                                       .collect(Collectors.toList());
        tracksByCountry = countryQuery.getCountries()
                                      .filter(this::isNonEnglishSpeaking)
                                      .map(country -> Pair.with(country,
                                                                tracksQuery.getTracks(country.getName())))
                                      .collect(Collectors.toList());
    }

    public void run() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SequenceBenchmark.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    public void streamUtils(Blackhole bh) {
        StreamUtils.zip(artistsByCountry.stream().filter(p -> p.getValue1().size() > 0),
                        tracksByCountry.stream().filter(p -> p.getValue1().size() > 0),
                        (l, r) -> Triplet.with(
                                l.getValue0(),
                                l.getValue1().get(0),
                                r.getValue1().get(0)))
                   .filter(distinctByKey(Triplet::getValue1))
                   .forEach(bh::consume);
    }

    @Benchmark
    public void stream(Blackhole bh) {
        Iterator<Pair<Country, List<Artist>>> iter = artistsByCountry.stream()
                                                                     .filter(p -> p.getValue1().size() > 0)
                                                                     .iterator();

        tracksByCountry.stream()
                       .filter(p -> p.getValue1().size() > 0)
                       .flatMap(r -> of(iter.next()).map(l -> Triplet.with(l.getValue0(),
                                                                           l.getValue1().get(0),
                                                                           r.getValue1().get(0))))
                       .filter(distinctByKey(Triplet::getValue1))
                       .forEach(bh::consume);
    }

    @Benchmark
    public void jayield(Blackhole bh) {
        Query.fromStream(artistsByCountry.stream().filter(p -> p.getValue1().size() > 0))
             .zip(
                     Query.fromStream(tracksByCountry.stream().filter(p -> p.getValue1().size() > 0)),
                     (l, r) -> Triplet.with(
                             l.getValue0(),
                             l.getValue1().get(0),
                             r.getValue1().get(0))

             ).traverse(bh::consume);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private boolean isNonEnglishSpeaking(Country country) {
        return country.getLanguages().stream().map(Language::getIso6391).noneMatch(ENGLISH::equals);
    }
}

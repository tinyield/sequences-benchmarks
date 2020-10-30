package com.github.tiniyield.sequences.benchmarks.zip;

import com.codepoetics.protonpack.StreamUtils;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ArtistsKt;
import com.github.tiniyield.sequences.benchmarks.kt.zip.TracksKt;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ZipTopArtistAndTrackByCountryKt;
import com.github.tiniyield.sequences.benchmarks.operations.CustomStreamOperations;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.last.fm.Artists;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.last.fm.Tracks;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.rest.countries.Countries;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Language;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import one.util.streamex.StreamEx;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.google.common.collect.Streams.zip;
import static java.util.Locale.ENGLISH;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ZipTopArtistAndTrackByCountryBenchmark {


    private Countries countries;
    private Artists artists;
    private Tracks tracks;


    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public Query<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(
            Query<Pair<Country, Query<Artist>>> artists,
            Query<Pair<Country, Query<Track>>> tracks) {

        return artists.zip(
                tracks,
                (l, r) -> Triplet.with(
                        l.getValue0(),
                        l.getValue1().findFirst().orElse(null),
                        r.getValue1().findFirst().orElse(null)
                )
        ).filter(distinctByKey(Triplet::getValue1));
    }

    public Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountryGuava(Stream<Pair<Country, Stream<Artist>>> artists,
                                                                                      Stream<Pair<Country, Stream<Track>>> tracks) {
        return zip(
                artists,
                tracks,
                (l, r) -> Triplet.with(
                        l.getValue0(),
                        l.getValue1().findFirst().orElse(null),
                        r.getValue1().findFirst().orElse(null)
                )
        ).filter(distinctByKey(Triplet::getValue1));
    }

    public Sequence<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(Sequence<Pair<Country, Sequence<Artist>>> artists,
                                                                                   Sequence<Pair<Country, Sequence<Track>>> tracks) {
        return SequencesKt.distinctBy(
                SequencesKt.map(
                        SequencesKt.zip(artists, tracks),
                        pair -> Triplet.with(
                                pair.getFirst().getValue0(),
                                SequencesKt.first(pair.getFirst().getValue1()),
                                SequencesKt.first(pair.getSecond().getValue1())
                        )
                ),
                Triplet::getValue1
        );
    }

    public Seq<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(Seq<Pair<Country, Seq<Artist>>> artists,
                                                                              Seq<Pair<Country, Seq<Track>>> tracks) {
        return artists.zip(tracks).map(entry -> {
            Pair<Country, Seq<Artist>> key = entry.v1;
            return Triplet.with(
                    key.getValue0(),
                    key.getValue1().findFirst().orElse(null),
                    entry.v2.getValue1().findFirst().orElse(null));
        }).distinct(Triplet::getValue1);
    }

    public Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountryProtonpack(Stream<Pair<Country, Stream<Artist>>> artists,
                                                                                           Stream<Pair<Country, Stream<Track>>> tracks) {
        return StreamUtils.zip(
                artists,
                tracks,
                (l, r) -> Triplet.with(
                        l.getValue0(),
                        l.getValue1().findFirst().orElse(null),
                        r.getValue1().findFirst().orElse(null)
                )
        ).filter(distinctByKey(Triplet::getValue1));
    }

    public StreamEx<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(
            StreamEx<Pair<Country, StreamEx<Artist>>> artists,
            StreamEx<Pair<Country, StreamEx<Track>>> tracks) {

        return artists.zipWith(tracks).map(entry -> {
            Pair<Country, StreamEx<Artist>> key = entry.getKey();
            return Triplet.with(
                    key.getValue0(),
                    key.getValue1().findFirst().orElse(null),
                    entry.getValue().getValue1().findFirst().orElse(null));
        }).distinct(Triplet::getValue1);
    }

    public Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(
            Stream<Pair<Country, Stream<Artist>>> artists,
            Stream<Pair<Country, Stream<Track>>> tracks) {

        return CustomStreamOperations.zip(artists,
                tracks,
                (l, r) -> Triplet.with(
                        l.getValue0(),
                        l.getValue1().findFirst().orElse(null),
                        r.getValue1().findFirst().orElse(null)
                )
        ).filter(distinctByKey(Triplet::getValue1));
    }

    public io.vavr.collection.Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(
            io.vavr.collection.Stream<Pair<Country, io.vavr.collection.Stream<Artist>>> artists,
            io.vavr.collection.Stream<Pair<Country, io.vavr.collection.Stream<Track>>> tracks) {

        return artists.zip(tracks).map(entry -> {
            Pair<Country, io.vavr.collection.Stream<Artist>> key = entry._1();
            return Triplet.with(
                    key.getValue0(),
                    key.getValue1().getOrNull(),
                    entry._2().getValue1().getOrNull());
        }).distinctBy(Triplet::getValue1);
    }

    public Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountryZipline(
            Stream<Pair<Country, Stream<Artist>>> artists,
            Stream<Pair<Country, Stream<Track>>> tracks) {

        Iterator<Pair<Country, Stream<Artist>>> iter = artists.iterator();
        return tracks.map(r -> {
            Track track = r.getValue1().findFirst().orElse(null);
            Pair<Country, Stream<Artist>> l = iter.next();
            return Triplet.with(l.getValue0(), l.getValue1().iterator().next(), track);
        }).filter(distinctByKey(Triplet::getValue1));
    }

    @Setup()
    public void setup() {
        countries = new Countries();
        artists = new Artists(countries);
        tracks = new Tracks(countries);
    }

    public io.vavr.collection.Stream<Triplet<Country, Artist, Track>> getVavr() {
        Predicate<Country> isNonEnglishSpeaking = country -> !io.vavr.collection.Stream.ofAll(country.getLanguages())
                .map(Language::getIso6391)
                .exists(ENGLISH.getLanguage()::equals);

        io.vavr.collection.Stream<Pair<Country, io.vavr.collection.Stream<Artist>>> artistsByCountry =
                io.vavr.collection.Stream.of(countries.data)
                        .filter(isNonEnglishSpeaking)
                        .filter(country -> artists.data.containsKey(country.getName()) && artists.data.get(country.getName()).length > 0)
                        .map(country -> Pair.with(country, io.vavr.collection.Stream.of(artists.data.get(country.getName()))));

        io.vavr.collection.Stream<Pair<Country, io.vavr.collection.Stream<Track>>> tracksByCountry =
                io.vavr.collection.Stream.of(countries.data)
                        .filter(isNonEnglishSpeaking)
                        .filter(country -> tracks.data.containsKey(country.getName()) && tracks.data.get(country.getName()).length > 0)
                        .map(country -> Pair.with(country, io.vavr.collection.Stream.of(tracks.data.get(country.getName()))));

        return zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry);
    }

    public StreamEx<Triplet<Country, Artist, Track>> getStreamEx() {
        Predicate<Country> isNonEnglishSpeaking = country -> StreamEx.of(country.getLanguages())
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        StreamEx<Pair<Country, StreamEx<Artist>>> artistsByCountry = StreamEx.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> artists.data.containsKey(country.getName()) && artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, StreamEx.of(artists.data.get(country.getName()))));

        StreamEx<Pair<Country, StreamEx<Track>>> tracksByCountry = StreamEx.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> tracks.data.containsKey(country.getName()) && tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, StreamEx.of(tracks.data.get(country.getName()))));

        return zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry);
    }

    public Stream<Triplet<Country, Artist, Track>> getZipline() {
        Predicate<Country> isNonEnglishSpeaking = country -> country.getLanguages().stream()
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        Stream<Pair<Country, Stream<Artist>>> artistsByCountry = Stream.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> artists.data.containsKey(country.getName()) && artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(artists.data.get(country.getName()))));

        Stream<Pair<Country, Stream<Track>>> tracksByCountry = Stream.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> tracks.data.containsKey(country.getName()) && tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(tracks.data.get(country.getName()))));

        return zipTopArtistAndTrackByCountryZipline(artistsByCountry, tracksByCountry);
    }

    public Stream<Triplet<Country, Artist, Track>> getProtonpack() {
        Predicate<Country> isNonEnglishSpeaking = country -> country.getLanguages().stream()
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        Stream<Pair<Country, Stream<Artist>>> artistsByCountry = Stream.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> artists.data.containsKey(country.getName()) && artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(artists.data.get(country.getName()))));

        Stream<Pair<Country, Stream<Track>>> tracksByCountry = Stream.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> tracks.data.containsKey(country.getName()) && tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(tracks.data.get(country.getName()))));

        return zipTopArtistAndTrackByCountryProtonpack(artistsByCountry, tracksByCountry);
    }

    public Stream<Triplet<Country, Artist, Track>> getStream() {
        Predicate<Country> isNonEnglishSpeaking = country -> country.getLanguages().stream()
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        Stream<Pair<Country, Stream<Artist>>> artistsByCountry = Stream.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> artists.data.containsKey(country.getName()) && artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(artists.data.get(country.getName()))));

        Stream<Pair<Country, Stream<Track>>> tracksByCountry = Stream.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> tracks.data.containsKey(country.getName()) && tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(tracks.data.get(country.getName()))));

        return zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry);
    }

    public Query<Triplet<Country, Artist, Track>> getQuery() {
        Predicate<Country> isNonEnglishSpeaking = country -> Query.fromList(country.getLanguages())
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        Query<Pair<Country, Query<Artist>>> artistsByCountry = Query.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> artists.data.containsKey(country.getName()) && artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Query.of(artists.data.get(country.getName()))));

        Query<Pair<Country, Query<Track>>> tracksByCountry = Query.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> tracks.data.containsKey(country.getName()) && tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Query.of(tracks.data.get(country.getName()))));

        return zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry);
    }

    public Stream<Triplet<Country, Artist, Track>> getGuava() {
        Predicate<Country> isNonEnglishSpeaking = country -> country.getLanguages().stream()
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        Stream<Pair<Country, Stream<Artist>>> artistsByCountry = Stream.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> artists.data.containsKey(country.getName()) && artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(artists.data.get(country.getName()))));

        Stream<Pair<Country, Stream<Track>>> tracksByCountry = Stream.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> tracks.data.containsKey(country.getName()) && tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(tracks.data.get(country.getName()))));

        return zipTopArtistAndTrackByCountryGuava(artistsByCountry, tracksByCountry);
    }

    public Seq<Triplet<Country, Artist, Track>> getJool() {
        Predicate<Country> isNonEnglishSpeaking = country -> Seq.seq(country.getLanguages())
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        Seq<Pair<Country, Seq<Artist>>> artistsByCountry = Seq.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> artists.data.containsKey(country.getName()) && artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Seq.of(artists.data.get(country.getName()))));

        Seq<Pair<Country, Seq<Track>>> tracksByCountry = Seq.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> tracks.data.containsKey(country.getName()) && tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Seq.of(tracks.data.get(country.getName()))));

        return zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry);
    }

    public Sequence<Triplet<Country, Artist, Track>> getKotlin() {
        return ZipTopArtistAndTrackByCountryKt.zipTopArtistAndTrackByCountry(ArtistsKt.artistsByCountry(), TracksKt.tracksByCountry());
    }

    public Sequence<Triplet<Country, Artist, Track>> getJKotlin() {
        Function1<Country, Boolean> isNonEnglishSpeaking = country -> {
            return SequencesKt.none(
                    SequencesKt.map(
                            CollectionsKt.asSequence(country.getLanguages()),
                            Language::getIso6391
                    ),
                    ENGLISH.getLanguage()::equals
            );
        };

        Sequence<Pair<Country, Sequence<Artist>>> artistsByCountry = SequencesKt.map(
                SequencesKt.filter(
                        SequencesKt.filter(
                                ArraysKt.asSequence(countries.data),
                                isNonEnglishSpeaking
                        ),
                        country -> artists.data.containsKey(country.getName()) && artists.data.get(country.getName()).length > 0
                ),
                country -> Pair.with(country, ArraysKt.asSequence(artists.data.get(country.getName())))
        );

        Sequence<Pair<Country, Sequence<Track>>> tracksByCountry = SequencesKt.map(
                SequencesKt.filter(
                        SequencesKt.filter(
                                ArraysKt.asSequence(countries.data),
                                isNonEnglishSpeaking
                        ),
                        country -> tracks.data.containsKey(country.getName()) && tracks.data.get(country.getName()).length > 0
                ),
                country -> Pair.with(country, ArraysKt.asSequence(tracks.data.get(country.getName())))
        );

        return zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry);
    }

    @Benchmark
    public final void stream(Blackhole bh) { // With Auxiliary Function
        getStream().forEach(bh::consume);
    }

    @Benchmark
    public final void streamEx(Blackhole bh) {
        getStreamEx().forEach(bh::consume);
    }

    @Benchmark
    public final void jayield(Blackhole bh) {
        getQuery().traverse(bh::consume);
    }

    @Benchmark
    public final void jool(Blackhole bh) {
        getJool().forEach(bh::consume);
    }

    // Other Sequences based benchmarks

    @Benchmark
    public final void vavr(Blackhole bh) {
        getVavr().forEach(bh::consume);
    }

    @Benchmark
    public final void protonpack(Blackhole bh) {
        getProtonpack().forEach(bh::consume);
    }

    @Benchmark
    public final void guava(Blackhole bh) {
        getGuava().forEach(bh::consume);
    }

    @Benchmark
    public final void zipline(Blackhole bh) {
        getZipline().forEach(bh::consume);
    }

    @Benchmark
    public final void kotlin(Blackhole bh) {
        SequencesKt.forEach(getKotlin(), elem -> {
            bh.consume(elem);
            return Unit.INSTANCE;
        });
    }

    @Benchmark
    public final void jkotlin(Blackhole bh) {
        SequencesKt.forEach(getJKotlin(), elem -> {
            bh.consume(elem);
            return Unit.INSTANCE;
        });
    }
}

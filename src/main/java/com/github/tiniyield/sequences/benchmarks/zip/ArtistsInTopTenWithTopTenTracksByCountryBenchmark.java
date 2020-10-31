package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.kt.zip.ArtistsInTopTenWithTopTenTracksByCountryKt;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ArtistsKt;
import com.github.tiniyield.sequences.benchmarks.kt.zip.TracksKt;
import com.github.tiniyield.sequences.benchmarks.operations.CustomStreamOperations;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.last.fm.Artists;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.last.fm.Tracks;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.rest.countries.Countries;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Language;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.google.common.collect.Streams;
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
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codepoetics.protonpack.StreamUtils.zip;
import static com.github.tiniyield.sequences.benchmarks.operations.common.BenchmarkConstants.TEN;
import static java.util.Locale.ENGLISH;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ArtistsInTopTenWithTopTenTracksByCountryBenchmark {

    private Countries countries;
    private Artists artists;
    private Tracks tracks;

    public Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountryProtonpack(Stream<Pair<Country,
            Stream<Artist>>> artists, Stream<Pair<Country, Stream<Track>>> tracks) {
        return zip(artists, tracks, (l, r) -> Triplet.with(l.getValue0(), l.getValue1(), r.getValue1()))
                .map(triplet -> {
                    List<String> topTenSongsArtistsNames = triplet.getValue2()
                            .limit(TEN)
                            .map(Track::getArtist)
                            .map(Artist::getName)
                            .collect(Collectors.toList());

                    List<Artist> topTenArtists = triplet.getValue1()
                            .limit(TEN)
                            .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                            .collect(Collectors.toList());
                    return Pair.with(triplet.getValue0(), topTenArtists);

                });
    }

    public Query<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(
            Query<Pair<Country, Query<Artist>>> artists,
            Query<Pair<Country, Query<Track>>> tracks) {
        return artists.zip(tracks, (l, r) -> Triplet.with(l.getValue0(), l.getValue1(), r.getValue1()))
                .map(triplet -> {
                    List<String> topTenSongsArtistsNames = triplet.getValue2()
                            .limit(TEN)
                            .map(Track::getArtist)
                            .map(Artist::getName)
                            .toList();

                    List<Artist> topTenArtists = triplet.getValue1()
                            .limit(TEN)
                            .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                            .toList();
                    return Pair.with(triplet.getValue0(), topTenArtists);

                });
    }

    public Seq<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(Seq<Pair<Country, Seq<Artist>>> artists,
                                                                                     Seq<Pair<Country, Seq<Track>>> tracks) {
        return artists.zip(tracks)
                .map(tuple -> Triplet.with(tuple.v1.getValue0(), tuple.v1.getValue1(), tuple.v2.getValue1()))
                .map(triplet -> {
                    List<String> topTenSongsArtistsNames = triplet.getValue2()
                            .limit(TEN)
                            .map(Track::getArtist)
                            .map(Artist::getName)
                            .toList();

                    List<Artist> topTenArtists = triplet.getValue1()
                            .limit(TEN)
                            .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                            .toList();
                    return Pair.with(triplet.getValue0(), topTenArtists);

                });
    }

    public Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountryGuava(
            Stream<Pair<Country,
                    Stream<Artist>>> artists,
            Stream<Pair<Country, Stream<Track>>> tracks) {
        return Streams.zip(artists, tracks, (l, r) -> Triplet.with(l.getValue0(), l.getValue1(), r.getValue1()))
                .map(triplet -> {
                    List<String> topTenSongsArtistsNames = triplet.getValue2()
                            .limit(TEN)
                            .map(Track::getArtist)
                            .map(Artist::getName)
                            .collect(Collectors.toList());

                    List<Artist> topTenArtists = triplet.getValue1()
                            .limit(TEN)
                            .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                            .collect(Collectors.toList());
                    return Pair.with(triplet.getValue0(), topTenArtists);
                });
    }

    public Sequence<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(Sequence<Pair<Country, Sequence<Artist>>> artists,
                                                                                          Sequence<Pair<Country, Sequence<Track>>> tracks) {
        return SequencesKt.map(
                SequencesKt.map(
                        SequencesKt.zip(artists, tracks),
                        pair -> Triplet.with(pair.component1().getValue0(), pair.component1().getValue1(), pair.component2().getValue1())
                ),
                triplet -> {
                    List<String> topTenSongsArtistsNames = SequencesKt.toList(
                            SequencesKt.map(
                                    SequencesKt.map(
                                            SequencesKt.take(triplet.getValue2(), TEN),
                                            Track::getArtist
                                    ),
                                    Artist::getName
                            )
                    );

                    List<Artist> topTenArtists = SequencesKt.toList(
                            SequencesKt.filter(
                                    SequencesKt.take(triplet.getValue1(), TEN),
                                    artist -> topTenSongsArtistsNames.contains(artist.getName())
                            )
                    );

                    return Pair.with(triplet.getValue0(), topTenArtists);
                }
        );
    }

    public StreamEx<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(
            StreamEx<Pair<Country, StreamEx<Artist>>> artists,
            StreamEx<Pair<Country, StreamEx<Track>>> tracks) {

        return artists
                .zipWith(tracks)
                .map((pair) -> Triplet.with(pair.getKey().getValue0(), pair.getKey().getValue1(), pair.getValue().getValue1()))
                .map(triplet -> {
                    List<String> topTenSongsArtistsNames = triplet.getValue2()
                            .limit(TEN)
                            .map(Track::getArtist)
                            .map(Artist::getName)
                            .collect(Collectors.toList());

                    List<Artist> topTenArtists = triplet.getValue1()
                            .limit(TEN)
                            .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                            .collect(Collectors.toList());
                    return Pair.with(triplet.getValue0(), topTenArtists);

                });
    }

    public Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(
            Stream<Pair<Country, Stream<Artist>>> artists,
            Stream<Pair<Country, Stream<Track>>> tracks) {

        return CustomStreamOperations.zip(artists, tracks, (l, r) -> Triplet.with(l.getValue0(), l.getValue1(), r.getValue1()))
                .map(triplet -> {
                    List<String> topTenSongsArtistsNames = triplet.getValue2()
                            .limit(TEN)
                            .map(Track::getArtist)
                            .map(Artist::getName)
                            .collect(Collectors.toList());

                    List<Artist> topTenArtists = triplet.getValue1()
                            .limit(TEN)
                            .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                            .collect(Collectors.toList());
                    return Pair.with(triplet.getValue0(), topTenArtists);

                });
    }

    public io.vavr.collection.Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(
            io.vavr.collection.Stream<Pair<Country, io.vavr.collection.Stream<Artist>>> artists,
            io.vavr.collection.Stream<Pair<Country, io.vavr.collection.Stream<Track>>> tracks) {

        return artists.zip(tracks)
                .map(tuple -> Triplet.with(tuple._1.getValue0(), tuple._1.getValue1(), tuple._2.getValue1()))
                .map(triplet -> {
                    io.vavr.collection.List<String> topTenSongsArtistsNames = triplet.getValue2()
                            .take(TEN)
                            .map(Track::getArtist)
                            .map(Artist::getName)
                            .toList();

                    java.util.List<Artist> topTenArtists = triplet.getValue1()
                            .take(TEN)
                            .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                            .collect(Collectors.toList());
                    return Pair.with(triplet.getValue0(), topTenArtists);

                });
    }

    public Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountryZipline(
            Stream<Pair<Country, Stream<Artist>>> artists,
            Stream<Pair<Country, Stream<Track>>> tracks) {

        Iterator<Pair<Country, Stream<Artist>>> iter = artists.iterator();
        return tracks.map(r -> {
            Pair<Country, Stream<Artist>> l = iter.next();
            return Triplet.with(l.getValue0(),
                    l.getValue1(),
                    r.getValue1());
        }).map(triplet -> {
            List<String> topTenSongsArtistsNames = triplet.getValue2()
                    .limit(TEN)
                    .map(Track::getArtist)
                    .map(Artist::getName)
                    .collect(Collectors.toList());

            List<Artist> topTenArtists = triplet.getValue1()
                    .limit(TEN)
                    .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                    .collect(Collectors.toList());
            return Pair.with(triplet.getValue0(), topTenArtists);

        });
    }

    @Setup()
    public void setup() {
        countries = new Countries();
        artists = new Artists(countries);
        tracks = new Tracks(countries);
    }

    public io.vavr.collection.Stream<Pair<Country, List<Artist>>> getVavr() {
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


        return artistsInTopTenWithTopTenTracksByCountry(artistsByCountry, tracksByCountry);
    }

    public StreamEx<Pair<Country, List<Artist>>> getStreamEx() {
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


        return artistsInTopTenWithTopTenTracksByCountry(artistsByCountry, tracksByCountry);
    }

    public Stream<Pair<Country, List<Artist>>> getZipline() {
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

        return artistsInTopTenWithTopTenTracksByCountryZipline(artistsByCountry, tracksByCountry);
    }

    public Stream<Pair<Country, List<Artist>>> getProtonpack() {
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

        return artistsInTopTenWithTopTenTracksByCountryProtonpack(artistsByCountry, tracksByCountry);
    }

    public Stream<Pair<Country, List<Artist>>> getStream() {
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

        return artistsInTopTenWithTopTenTracksByCountry(artistsByCountry, tracksByCountry);
    }

    public Query<Pair<Country, List<Artist>>> getQuery() {
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

        return artistsInTopTenWithTopTenTracksByCountry(artistsByCountry, tracksByCountry);
    }

    public Stream<Pair<Country, List<Artist>>> getGuava() {
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

        return artistsInTopTenWithTopTenTracksByCountryGuava(artistsByCountry, tracksByCountry);
    }

    public Seq<Pair<Country, List<Artist>>> getJool() {
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

        return artistsInTopTenWithTopTenTracksByCountry(artistsByCountry, tracksByCountry);
    }

    public Sequence<Pair<Country, List<Artist>>> getKotlin() {
        return ArtistsInTopTenWithTopTenTracksByCountryKt.artistsInTopTenWithTopTenTracksByCountry(ArtistsKt.artistsByCountry(), TracksKt.tracksByCountry());
    }

    public Sequence<Pair<Country, List<Artist>>> getJKotlin() {
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

        return artistsInTopTenWithTopTenTracksByCountry(artistsByCountry, tracksByCountry);
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

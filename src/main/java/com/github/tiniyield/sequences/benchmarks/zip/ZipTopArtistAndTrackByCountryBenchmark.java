package com.github.tiniyield.sequences.benchmarks.zip;

import com.codepoetics.protonpack.StreamUtils;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ArtistsKt;
import com.github.tiniyield.sequences.benchmarks.kt.zip.TracksKt;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ZipTopArtistAndTrackByCountryKt;
import com.github.tiniyield.sequences.benchmarks.common.data.providers.last.fm.Artists;
import com.github.tiniyield.sequences.benchmarks.common.data.providers.last.fm.Tracks;
import com.github.tiniyield.sequences.benchmarks.common.data.providers.rest.countries.Countries;
import com.github.tiniyield.sequences.benchmarks.common.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.common.model.country.Language;
import com.github.tiniyield.sequences.benchmarks.common.model.track.Track;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.sequences.Sequence;
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
import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.collections.CollectionsKt.asSequence;
import static kotlin.sequences.SequencesKt.distinctBy;
import static kotlin.sequences.SequencesKt.filter;
import static kotlin.sequences.SequencesKt.first;
import static kotlin.sequences.SequencesKt.forEach;
import static kotlin.sequences.SequencesKt.map;
import static kotlin.sequences.SequencesKt.none;
import static kotlin.sequences.SequencesKt.zip;

/**
 * ZipTopArtistAndTrackByCountryBenchmark
 * Benchmarks creating two different sequences, one consisting of the top 50 Artists
 * (provided by [Last.fm](https://www.last.fm/api/)) of each non english speaking
 * country (provided by [REST Countries](https://restcountries.eu/)) and the other
 * the exact same thing but for the top 50 Tracks.
 * Then zipping both sequences into a Trio of Country, First Artist and First Track and
 * retrieving the distinct elements by Artist.
 *
 * Pipelines:
 * * Sequence of Artists:
 * * * Sequence.of(countries)
 * * * .filter(isNonEnglishSpeaking)
 * * * .filter(hasArtists)
 * * * .map(Pair.of(country, artists));
 *
 * * Sequence of Tracks:
 * * * Sequence.of(countries)
 * * * .filter(isNonEnglishSpeaking)
 * * * .filter(hasTracks)
 * * * .map(Pair.of(country, tracks));
 *
 * * Pipeline:
 * * * artistsByCountry.zip(tracksByCountry, Trio.of(country, topArtist, topTrack))
 * * * .distinctBy(artist)
 * * * .forEach(bh::consume)
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ZipTopArtistAndTrackByCountryBenchmark {

    /**
     * Provider for the Country data
     */
    public Countries countries;

    /**
     * Provider for the Artist data by country
     */
    public Artists artists;

    /**
     * Provider for the Track data by country
     */
    public Tracks tracks;

    /**
     * Sets up the providers to be used in this benchmark
     */
    @Setup()
    public void setup() {
        countries = new Countries();
        artists = new Artists(countries);
        tracks = new Tracks(countries);
    }


    /**
     * Runs this benchmark using {@link Stream}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void stream(Blackhole bh) {
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

        zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link StreamEx}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void streamEx(Blackhole bh) {
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

        zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link Query}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void jayield(Blackhole bh) {
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

        zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry).traverse(bh::consume);
    }

    /**
     * Runs this benchmark using {@link Seq}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void jool(Blackhole bh) {
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

        zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link io.vavr.collection.Stream}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void vavr(Blackhole bh) {
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

        zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Protonpack in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void protonpack(Blackhole bh) {
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

        zipTopArtistAndTrackByCountryProtonpack(artistsByCountry, tracksByCountry).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Guava in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void guava(Blackhole bh) {
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

        zipTopArtistAndTrackByCountryGuava(artistsByCountry, tracksByCountry).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s and the
     * zipline approach in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void zipline(Blackhole bh) {
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

        zipTopArtistAndTrackByCountryZipline(artistsByCountry, tracksByCountry).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void kotlin(Blackhole bh) {
        forEach(
                ZipTopArtistAndTrackByCountryKt.zipTopArtistAndTrackByCountry(ArtistsKt.artistsByCountry(), TracksKt.tracksByCountry()),
                elem -> {
                    bh.consume(elem);
                    return Unit.INSTANCE;
                }
        );
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Java in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void jkotlin(Blackhole bh) {
        Function1<Country, Boolean> isNonEnglishSpeaking = country -> none(
                map(asSequence(country.getLanguages()), Language::getIso6391),
                ENGLISH.getLanguage()::equals
        );

        Sequence<Pair<Country, Sequence<Artist>>> artistsByCountry = map(
                filter(
                        filter(asSequence(countries.data), isNonEnglishSpeaking),
                        country -> artists.data.containsKey(country.getName()) && artists.data.get(country.getName()).length > 0
                ),
                country -> Pair.with(country, asSequence(artists.data.get(country.getName())))
        );

        Sequence<Pair<Country, Sequence<Track>>> tracksByCountry = map(
                filter(
                        filter(asSequence(countries.data), isNonEnglishSpeaking),
                        country -> tracks.data.containsKey(country.getName()) && tracks.data.get(country.getName()).length > 0
                ),
                country -> Pair.with(country, asSequence(tracks.data.get(country.getName())))
        );
        forEach(zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry), elem -> {
            bh.consume(elem);
            return Unit.INSTANCE;
        });
    }

    /**
     * Takes two {@link Stream}s and zips them into a Trio of Country, First Artist and First Track,
     * and filters the resulting sequence by distinct Artists
     * @param artists sequence of artists by country
     * @param tracks sequence of tracks by country
     * @return A {@link Stream} consisting of Trios of Country, First Artist and First Track
     * filtered to have only distinct artists
     */
    public Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(
            Stream<Pair<Country, Stream<Artist>>> artists,
            Stream<Pair<Country, Stream<Track>>> tracks) {

        return StreamZipOperation.zip(artists,
                tracks,
                (l, r) -> Triplet.with(
                        l.getValue0(),
                        l.getValue1().findFirst().orElse(null),
                        r.getValue1().findFirst().orElse(null)
                )
        ).filter(distinctByKey(Triplet::getValue1));
    }

    /**
     * Takes two {@link StreamEx}s and zips them into a Trio of Country, First Artist and First Track,
     * and filters the resulting sequence by distinct Artists
     * @param artists sequence of artists by country
     * @param tracks sequence of tracks by country
     * @return A {@link StreamEx} consisting of Trios of Country, First Artist and First Track
     * filtered to have only distinct artists
     */
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

    /**
     * Takes two {@link Query}s and zips them into a Trio of Country, First Artist and First Track,
     * and filters the resulting sequence by distinct Artists
     * @param artists sequence of artists by country
     * @param tracks sequence of tracks by country
     * @return A {@link Query} consisting of Trios of Country, First Artist and First Track
     * filtered to have only distinct artists
     */
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

    /**
     * Takes two {@link Seq}s and zips them into a Trio of Country, First Artist and First Track,
     * and filters the resulting sequence by distinct Artists
     * @param artists sequence of artists by country
     * @param tracks sequence of tracks by country
     * @return A {@link Seq} consisting of Trios of Country, First Artist and First Track
     * filtered to have only distinct artists
     */
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

    /**
     * Takes two {@link io.vavr.collection.Stream}s and zips them into a Trio of Country, First Artist and First Track,
     * and filters the resulting sequence by distinct Artists
     * @param artists sequence of artists by country
     * @param tracks sequence of tracks by country
     * @return A {@link io.vavr.collection.Stream} consisting of Trios of Country, First Artist and First Track
     * filtered to have only distinct artists
     */
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

    /**
     * Takes two {@link Stream}s and zips them into a Trio of Country, First Artist and First Track,
     * and filters the resulting sequence by distinct Artists, using Protonpack
     * @param artists sequence of artists by country
     * @param tracks sequence of tracks by country
     * @return A {@link Stream} consisting of Trios of Country, First Artist and First Track
     * filtered to have only distinct artists
     */
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

    /**
     * Takes two {@link Stream}s and zips them into a Trio of Country, First Artist and First Track,
     * and filters the resulting sequence by distinct Artists, using Guava
     * @param artists sequence of artists by country
     * @param tracks sequence of tracks by country
     * @return A {@link Stream} consisting of Trios of Country, First Artist and First Track
     * filtered to have only distinct artists
     */
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

    /**
     * Takes two {@link Stream}s and zips them into a Trio of Country, First Artist and First Track,
     * and filters the resulting sequence by distinct Artists, using the zipline approach
     * @param artists sequence of artists by country
     * @param tracks sequence of tracks by country
     * @return A {@link Stream} consisting of Trios of Country, First Artist and First Track
     * filtered to have only distinct artists
     */
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

    /**
     * Takes two Kotlin {@link Sequence}s in Java and zips them into a Trio of Country, First Artist and First Track,
     * and filters the resulting sequence by distinct Artists, using Protonpack
     * @param artists sequence of artists by country
     * @param tracks sequence of tracks by country
     * @return A Kotlin {@link Sequence} consisting of Trios of Country, First Artist and First Track
     * filtered to have only distinct artists
     */
    public Sequence<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(Sequence<Pair<Country, Sequence<Artist>>> artists,
                                                                                   Sequence<Pair<Country, Sequence<Track>>> tracks) {
        return distinctBy(
                map(
                        zip(artists, tracks),
                        pair -> Triplet.with(
                                pair.getFirst().getValue0(),
                                first(pair.getFirst().getValue1()),
                                first(pair.getSecond().getValue1()
                                )
                        )
                ),
                Triplet::getValue1
        );
    }

    /**
     * This method return a Predicate that will let through only distinct elements according to the {@param keyExtractor}
     * @param keyExtractor the extractor of the elements identity
     * @return a Predicate that will distinct elements by a {@param keyExtractor}
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}

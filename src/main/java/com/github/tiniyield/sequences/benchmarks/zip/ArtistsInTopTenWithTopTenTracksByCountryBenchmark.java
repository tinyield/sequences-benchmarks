package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.common.data.providers.last.fm.Artists;
import com.github.tiniyield.sequences.benchmarks.common.data.providers.last.fm.Tracks;
import com.github.tiniyield.sequences.benchmarks.common.data.providers.rest.countries.Countries;
import com.github.tiniyield.sequences.benchmarks.common.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.common.model.country.Language;
import com.github.tiniyield.sequences.benchmarks.common.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ArtistsInTopTenWithTopTenTracksByCountryKt;
import com.google.common.collect.Streams;
import com.tinyield.Sek;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.impl.factory.Lists;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codepoetics.protonpack.StreamUtils.zip;
import static java.util.Locale.ENGLISH;
import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.collections.CollectionsKt.asSequence;
import static kotlin.sequences.SequencesKt.filter;
import static kotlin.sequences.SequencesKt.forEach;
import static kotlin.sequences.SequencesKt.map;
import static kotlin.sequences.SequencesKt.none;
import static kotlin.sequences.SequencesKt.take;
import static kotlin.sequences.SequencesKt.toList;
import static kotlin.sequences.SequencesKt.zip;

/**
 * ArtistsInTopTenWithTopTenTracksByCountryBenchmark
 * Benchmarks creating two different sequences, one consisting of the top 50 Artists
 * (provided by [Last.fm](https://www.last.fm/api/)) of each non english speaking
 * country (provided by [REST Countries](https://restcountries.eu/)) and the other
 * the exact same thing but for the top 50 Tracks.
 * Then, for each Country, we take the top ten Artists and top ten Track artist's
 * names and zip them into a Trio. After, the top ten artists are filtered by their
 * presence in the top ten Track artist's name, returning a Pair with the Country
 * and the resulting Sequence.
 * <p>
 * Pipelines:
 * * Sequence of Artists:
 * * * Sequence.of(countries)
 * * * .filter(isNonEnglishSpeaking)
 * * * .filter(hasArtists)
 * * * .map(Pair.of(country, artists));
 * <p>
 * * Sequence of Tracks:
 * * * Sequence.of(countries)
 * * * .filter(isNonEnglishSpeaking)
 * * * .filter(hasTracks)
 * * * .map(Pair.of(country, tracks));
 * <p>
 * * Pipeline:
 * * * artistsByCountry.zip(tracksByCountry, Trio.of(country, artists, tracks))
 * * * .map(Pair.of(country, top10ArtistsWithSongsInTop10))
 * * * .forEach(bh::consume)
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ArtistsInTopTenWithTopTenTracksByCountryBenchmark {

    /**
     * The constant 10 for use in Top 10 limits
     */
    public static final int TEN = 10;

    /**
     * Provider for the Country data
     */
    public static final Countries countries = new Countries();

    /**
     * Provider for the Artist data by country
     */
    public static final Artists artists = new Artists(countries);

    /**
     * Provider for the Track data by country
     */
    public static final Tracks tracks = new Tracks(countries);

    /**
     * This method takes two {@link Stream}s and for each Country, it takes the top ten Artists and the top ten Tracks
     * artist's names and zip them into a Trio. After that it filters the top ten artists by their presence in the
     * top ten Tracks artist's names, returning a Pair with the Country and the resulting Sequence of Artists.
     *
     * @return A {@link Stream} consisting of Pairs of Country and Artists that are in that country's top ten and also
     * have tracks in the top ten of the same country
     */
    public static final Stream<Pair<Country, List<Artist>>> streamPipeline() {
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

        return StreamZipOperation
                .zip(artistsByCountry, tracksByCountry, (l, r) -> Triplet.with(l.getValue0(), l.getValue1(), r.getValue1()))
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

    /**
     * This method takes two {@link StreamEx}s and for each Country, it takes the top ten Artists and the top ten Tracks
     * artist's names and zip them into a Trio. After that it filters the top ten artists by their presence in the
     * top ten Tracks artist's names, returning a Pair with the Country and the resulting Sequence of Artists.
     *
     * @return A {@link StreamEx} consisting of Pairs of Country and Artists that are in that country's top ten and also
     * have tracks in the top ten of the same country
     */
    public static final StreamEx<Pair<Country, List<Artist>>> streamExPipeline() {
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

        return artistsByCountry
                .zipWith(tracksByCountry)
                .map(pair -> Triplet.with(pair.getKey().getValue0(), pair.getKey().getValue1(), pair.getValue().getValue1()))
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

    /**
     * This method takes two {@link Query}s and for each Country, it takes the top ten Artists and the top ten Tracks
     * artist's names and zip them into a Trio. After that it filters the top ten artists by their presence in the
     * top ten Tracks artist's names, returning a Pair with the Country and the resulting Sequence of Artists.
     *
     * @return A {@link Query} consisting of Pairs of Country and Artists that are in that country's top ten and also
     * have tracks in the top ten of the same country
     */
    public static final Query<Pair<Country, List<Artist>>> jayieldPipeline() {
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

        return artistsByCountry
                .zip(tracksByCountry, (l, r) -> Triplet.with(l.getValue0(), l.getValue1(), r.getValue1()))
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

    /**
     * This method takes two {@link Seq}s and for each Country, it takes the top ten Artists and the top ten Tracks
     * artist's names and zip them into a Trio. After that it filters the top ten artists by their presence in the
     * top ten Tracks artist's names, returning a Pair with the Country and the resulting Sequence of Artists.
     *
     * @return A {@link Seq} consisting of Pairs of Country and Artists that are in that country's top ten and also
     * have tracks in the top ten of the same country
     */
    public static final Seq<Pair<Country, List<Artist>>> joolPipeline() {
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

        return artistsByCountry
                .zip(tracksByCountry)
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

    /**
     * This method takes two {@link io.vavr.collection.Stream}s and for each Country, it takes the top ten Artists
     * and the top ten Tracks artist's names and zip them into a Trio. After that it filters the top ten artists by
     * their presence in the top ten Tracks artist's names, returning a Pair with the Country and the resulting Sequence
     * of Artists.
     *
     * @return A {@link io.vavr.collection.Stream} consisting of Pairs of Country and Artists that are in that country's
     * top ten and also have tracks in the top ten of the same country
     */
    public static final io.vavr.collection.Stream<Pair<Country, List<Artist>>> vavrPipeline() {
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

        return artistsByCountry
                .zip(tracksByCountry)
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

    /**
     * This method takes two {@link Stream}s and for each Country, it takes the top ten Artists and the top ten Tracks
     * artist's names and zip them into a Trio. After that it filters the top ten artists by their presence in the
     * top ten Tracks artist's names, returning a Pair with the Country and the resulting Sequence of Artists, using Protonpack.
     *
     * @return A {@link Stream} consisting of Pairs of Country and Artists that are in that country's top ten and also
     * have tracks in the top ten of the same country
     */
    public static final Stream<Pair<Country, List<Artist>>> protonpackPipeline() {
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
        return zip(artistsByCountry, tracksByCountry, (l, r) -> Triplet.with(l.getValue0(), l.getValue1(), r.getValue1()))
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

    /**
     * This method takes two {@link Stream}s and for each Country, it takes the top ten Artists and the top ten Tracks
     * artist's names and zip them into a Trio. After that it filters the top ten artists by their presence in the
     * top ten Tracks artist's names, returning a Pair with the Country and the resulting Sequence of Artists, using Guava.
     *
     * @return A {@link Stream} consisting of Pairs of Country and Artists that are in that country's top ten and also
     * have tracks in the top ten of the same country
     */
    public static final Stream<Pair<Country, List<Artist>>> guavaPipeline() {
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

        return Streams
                .zip(artistsByCountry, tracksByCountry, (l, r) -> Triplet.with(l.getValue0(), l.getValue1(), r.getValue1()))
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

    /**
     * This method takes two {@link Stream}s and for each Country, it takes the top ten Artists and the top ten Tracks
     * artist's names and zip them into a Trio. After that it filters the top ten artists by their presence in the
     * top ten Tracks artist's names, returning a Pair with the Country and the resulting Sequence of Artists,
     * using the zipline approach.
     *
     * @return A {@link Stream} consisting of Pairs of Country and Artists that are in that country's top ten and also
     * have tracks in the top ten of the same country
     */
    public static final Stream<Pair<Country, List<Artist>>> ziplinePipeline() {
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

        Iterator<Pair<Country, Stream<Artist>>> iter = artistsByCountry.iterator();
        return tracksByCountry.map(r -> {
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

    /**
     * This method takes two Kotlin {@link Sequence}s and for each Country, it takes the top ten Artists and the top ten Tracks
     * artist's names and zip them into a Trio. After that it filters the top ten artists by their presence in the
     * top ten Tracks artist's names, returning a Pair with the Country and the resulting Sequence of Artists
     *
     * @return A Kotlin {@link Sequence} in Java consisting of Pairs of Country and Artists that are in that country's top ten and also
     * have tracks in the top ten of the same country
     */
    public static final Sequence<Pair<Country, List<Artist>>> jKotlinPipeline() {
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

        return map(
                map(
                        zip(artistsByCountry, tracksByCountry),
                        pair -> Triplet.with(pair.component1().getValue0(), pair.component1().getValue1(), pair.component2().getValue1())
                ),
                triplet -> {
                    List<String> topTenSongsArtistsNames = toList(
                            map(
                                    map(take(triplet.getValue2(), TEN), Track::getArtist),
                                    Artist::getName
                            )
                    );

                    List<Artist> topTenArtists = toList(
                            filter(
                                    take(triplet.getValue1(), TEN),
                                    artist -> topTenSongsArtistsNames.contains(artist.getName())
                            )
                    );

                    return Pair.with(triplet.getValue0(), topTenArtists);
                }
        );
    }

    /**
     * This method takes two {@link LazyIterable}s and for each Country, it takes the top ten Artists and the top ten Tracks
     * artist's names and zip them into a Trio. After that it filters the top ten artists by their presence in the
     * top ten Tracks artist's names, returning a Pair with the Country and the resulting Sequence of Artists.
     *
     * @return A {@link LazyIterable} consisting of Pairs of Country and Artists that are in that country's top ten and also
     * have tracks in the top ten of the same country
     */
    public static final LazyIterable<Pair<Country, List<Artist>>> eclipsePipeline() {
        org.eclipse.collections.api.block.predicate.Predicate<Country> isNonEnglishSpeaking =
                country -> Lists.immutable.ofAll(country.getLanguages()).asLazy()
                        .collect(Language::getIso6391)
                        .noneSatisfy(ENGLISH.getLanguage()::equals);

        LazyIterable<Pair<Country, LazyIterable<Artist>>> artistsByCountry = Lists.immutable.of(countries.data).asLazy()
                .select(isNonEnglishSpeaking)
                .select(country -> artists.data.containsKey(country.getName()) && artists.data.get(country.getName()).length > 0)
                .collect(country -> Pair.with(country, Lists.immutable.of(artists.data.get(country.getName())).asLazy()));

        LazyIterable<Pair<Country, LazyIterable<Track>>> tracksByCountry = Lists.immutable.of(countries.data).asLazy()
                .select(isNonEnglishSpeaking)
                .select(country -> tracks.data.containsKey(country.getName()) && tracks.data.get(country.getName()).length > 0)
                .collect(country -> Pair.with(country, Lists.immutable.of(tracks.data.get(country.getName())).asLazy()));

        return artistsByCountry
                .zip(tracksByCountry)
                .collect(p -> Triplet.with(p.getOne().getValue0(), p.getOne().getValue1(), p.getTwo().getValue1()))
                .collect(triplet -> {
                    List<String> topTenSongsArtistsNames = triplet.getValue2()
                            .take(TEN)
                            .collect(Track::getArtist)
                            .collect(Artist::getName)
                            .toList();

                    List<Artist> topTenArtists = triplet.getValue1()
                            .take(TEN)
                            .select(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                            .toList();
                    return Pair.with(triplet.getValue0(), topTenArtists);

                });
    }

    /**
     * This method takes two {@link Sek}s and for each Country, it takes the top ten Artists and the top ten Tracks
     * artist's names and zip them into a Trio. After that it filters the top ten artists by their presence in the
     * top ten Tracks artist's names, returning a Pair with the Country and the resulting Sequence of Artists.
     *
     * @return A {@link Sek} consisting of Pairs of Country and Artists that are in that country's top ten and also
     * have tracks in the top ten of the same country
     */
    public static final Sek<Pair<Country, List<Artist>>> sekPipeline() {
        Predicate<Country> isNonEnglishSpeaking = country -> Sek.of(country.getLanguages())
                .map(Language::getIso6391)
                .none(ENGLISH.getLanguage()::equals);

        Sek<Pair<Country, Sek<Artist>>> artistsByCountry = Sek.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> artists.data.containsKey(country.getName()) && artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Sek.of(artists.data.get(country.getName()))));

        Sek<Pair<Country, Sek<Track>>> tracksByCountry = Sek.of(countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> tracks.data.containsKey(country.getName()) && tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Sek.of(tracks.data.get(country.getName()))));

        return artistsByCountry.zip(tracksByCountry, (l, r) -> Triplet.with(l.getValue0(), l.getValue1(), r.getValue1()))
                .map(triplet -> {
                    List<String> topTenSongsArtistsNames = triplet.getValue2()
                            .take(TEN)
                            .map(Track::getArtist)
                            .map(Artist::getName)
                            .toList();

                    List<Artist> topTenArtists = triplet.getValue1()
                            .take(TEN)
                            .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                            .toList();
                    return Pair.with(triplet.getValue0(), topTenArtists);

                });
    }

    /**
     * Runs this benchmark using {@link Stream}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void stream(Blackhole bh) {
        streamPipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link StreamEx}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void streamEx(Blackhole bh) {
        streamExPipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link Query}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void jayield(Blackhole bh) {
        jayieldPipeline().traverse(bh::consume);
    }

    /**
     * Runs this benchmark using {@link Seq}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void jool(Blackhole bh) {
        joolPipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link io.vavr.collection.Stream}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void vavr(Blackhole bh) {
        vavrPipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Protonpack in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void protonpack(Blackhole bh) {
        protonpackPipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Guava in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void guava(Blackhole bh) {
        guavaPipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s and the
     * zipline approach in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void zipline(Blackhole bh) {
        ziplinePipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void kotlin(Blackhole bh) {
        forEach(
                ArtistsInTopTenWithTopTenTracksByCountryKt.artistsInTopTenWithTopTenTracksByCountry(),
                elem -> {
                    bh.consume(elem);
                    return Unit.INSTANCE;
                }
        );
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Java in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void jkotlin(Blackhole bh) {
        forEach(
                jKotlinPipeline(),
                elem -> {
                    bh.consume(elem);
                    return Unit.INSTANCE;
                }
        );
    }

    /**
     * Runs this benchmark using {@link LazyIterable}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void eclipse(Blackhole bh) {
        eclipsePipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link Sek}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void sek(Blackhole bh) {
        sekPipeline().forEach(bh::consume);
    }


}

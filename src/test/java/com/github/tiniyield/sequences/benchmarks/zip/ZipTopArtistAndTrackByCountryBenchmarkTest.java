package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.kt.zip.ArtistsKt;
import com.github.tiniyield.sequences.benchmarks.kt.zip.TracksKt;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ZipTopArtistAndTrackByCountryKt;
import com.github.tiniyield.sequences.benchmarks.common.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.common.model.country.Language;
import com.github.tiniyield.sequences.benchmarks.common.model.track.Track;
import io.vavr.collection.Stream;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import one.util.streamex.StreamEx;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Locale.ENGLISH;
import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.sequences.SequencesKt.filter;
import static kotlin.sequences.SequencesKt.map;
import static kotlin.sequences.SequencesKt.none;
import static org.testng.Assert.assertTrue;

public class ZipTopArtistAndTrackByCountryBenchmarkTest {

    private ZipTopArtistAndTrackByCountryBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new ZipTopArtistAndTrackByCountryBenchmark();
        instance.setup();
    }

    @Test
    public void testSameResultsZipTopArtistAndTrackByCountry() {
        List<Triplet<Country, Artist, Track>> stream = getStream().collect(Collectors.toList());
        List<Triplet<Country, Artist, Track>> streamEx = getStreamEx().toList();
        List<Triplet<Country, Artist, Track>> query = getQuery().toList();
        List<Triplet<Country, Artist, Track>> jool = getJool().toList();
        List<Triplet<Country, Artist, Track>> vavr = getVavr().toJavaList();
        List<Triplet<Country, Artist, Track>> protonpack = getProtonpack().collect(Collectors.toList());
        List<Triplet<Country, Artist, Track>> guava = getGuava().collect(Collectors.toList());
        List<Triplet<Country, Artist, Track>> kotlin = SequencesKt.toList(getKotlin());
        List<Triplet<Country, Artist, Track>> jKotlin = SequencesKt.toList(getJKotlin());
        List<Triplet<Country, Artist, Track>> zipline = getZipline().collect(Collectors.toList());

        assertTrue(stream.size() == streamEx.size() && stream.containsAll(streamEx) && streamEx.containsAll(stream));
        assertTrue(stream.size() == query.size() && stream.containsAll(query) && query.containsAll(stream));
        assertTrue(stream.size() == jool.size() && stream.containsAll(jool) && jool.containsAll(stream));
        assertTrue(stream.size() == vavr.size() && stream.containsAll(vavr) && vavr.containsAll(stream));
        assertTrue(stream.size() == protonpack.size() && stream.containsAll(protonpack) && protonpack.containsAll(stream));
        assertTrue(stream.size() == guava.size() && stream.containsAll(guava) && guava.containsAll(stream));
        assertTrue(stream.size() == jKotlin.size() && stream.containsAll(jKotlin) && jKotlin.containsAll(stream));
        assertTrue(stream.size() == zipline.size() && stream.containsAll(zipline) && zipline.containsAll(stream));

        assertTrue(stream.size() == kotlin.size());
        for (int i = 0; i < stream.size(); i++) {
            Triplet<Country, Artist, Track> a = stream.get(i);
            Triplet<Country, Artist, Track> b = kotlin.get(i);
            assertTrue(
                    a.getValue0().equals(b.getValue0()) &&
                            a.getValue1().equals(b.getValue1()) &&
                            a.getValue2().equals(b.getValue2())
            );
        }
    }


    public io.vavr.collection.Stream<Triplet<Country, Artist, Track>> getVavr() {
        Predicate<Country> isNonEnglishSpeaking = country -> !io.vavr.collection.Stream.ofAll(country.getLanguages())
                .map(Language::getIso6391)
                .exists(ENGLISH.getLanguage()::equals);

        io.vavr.collection.Stream<Pair<Country, Stream<Artist>>> artistsByCountry =
                io.vavr.collection.Stream.of(instance.countries.data)
                        .filter(isNonEnglishSpeaking)
                        .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                        .map(country -> Pair.with(country, io.vavr.collection.Stream.of(instance.artists.data.get(country.getName()))));

        io.vavr.collection.Stream<Pair<Country, io.vavr.collection.Stream<Track>>> tracksByCountry =
                io.vavr.collection.Stream.of(instance.countries.data)
                        .filter(isNonEnglishSpeaking)
                        .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                        .map(country -> Pair.with(country, io.vavr.collection.Stream.of(instance.tracks.data.get(country.getName()))));

        return instance.zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry);
    }

    public StreamEx<Triplet<Country, Artist, Track>> getStreamEx() {
        Predicate<Country> isNonEnglishSpeaking = country -> StreamEx.of(country.getLanguages())
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        StreamEx<Pair<Country, StreamEx<Artist>>> artistsByCountry = StreamEx.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, StreamEx.of(instance.artists.data.get(country.getName()))));

        StreamEx<Pair<Country, StreamEx<Track>>> tracksByCountry = StreamEx.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, StreamEx.of(instance.tracks.data.get(country.getName()))));

        return instance.zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry);
    }

    public java.util.stream.Stream<Triplet<Country, Artist, Track>> getZipline() {
        Predicate<Country> isNonEnglishSpeaking = country -> country.getLanguages().stream()
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        java.util.stream.Stream<Pair<Country, java.util.stream.Stream<Artist>>> artistsByCountry = java.util.stream.Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, java.util.stream.Stream.of(instance.artists.data.get(country.getName()))));

        java.util.stream.Stream<Pair<Country, java.util.stream.Stream<Track>>> tracksByCountry = java.util.stream.Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, java.util.stream.Stream.of(instance.tracks.data.get(country.getName()))));

        return instance.zipTopArtistAndTrackByCountryZipline(artistsByCountry, tracksByCountry);
    }

    public java.util.stream.Stream<Triplet<Country, Artist, Track>> getProtonpack() {
        Predicate<Country> isNonEnglishSpeaking = country -> country.getLanguages().stream()
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        java.util.stream.Stream<Pair<Country, java.util.stream.Stream<Artist>>> artistsByCountry = java.util.stream.Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, java.util.stream.Stream.of(instance.artists.data.get(country.getName()))));

        java.util.stream.Stream<Pair<Country, java.util.stream.Stream<Track>>> tracksByCountry = java.util.stream.Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, java.util.stream.Stream.of(instance.tracks.data.get(country.getName()))));

        return instance.zipTopArtistAndTrackByCountryProtonpack(artistsByCountry, tracksByCountry);
    }

    public java.util.stream.Stream<Triplet<Country, Artist, Track>> getStream() {
        Predicate<Country> isNonEnglishSpeaking = country -> country.getLanguages().stream()
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        java.util.stream.Stream<Pair<Country, java.util.stream.Stream<Artist>>> artistsByCountry = java.util.stream.Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, java.util.stream.Stream.of(instance.artists.data.get(country.getName()))));

        java.util.stream.Stream<Pair<Country, java.util.stream.Stream<Track>>> tracksByCountry = java.util.stream.Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, java.util.stream.Stream.of(instance.tracks.data.get(country.getName()))));

        return instance.zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry);
    }

    public Query<Triplet<Country, Artist, Track>> getQuery() {
        Predicate<Country> isNonEnglishSpeaking = country -> Query.fromList(country.getLanguages())
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        Query<Pair<Country, Query<Artist>>> artistsByCountry = Query.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Query.of(instance.artists.data.get(country.getName()))));

        Query<Pair<Country, Query<Track>>> tracksByCountry = Query.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Query.of(instance.tracks.data.get(country.getName()))));

        return instance.zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry);
    }

    public java.util.stream.Stream<Triplet<Country, Artist, Track>> getGuava() {
        Predicate<Country> isNonEnglishSpeaking = country -> country.getLanguages().stream()
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        java.util.stream.Stream<Pair<Country, java.util.stream.Stream<Artist>>> artistsByCountry = java.util.stream.Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, java.util.stream.Stream.of(instance.artists.data.get(country.getName()))));

        java.util.stream.Stream<Pair<Country, java.util.stream.Stream<Track>>> tracksByCountry = java.util.stream.Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, java.util.stream.Stream.of(instance.tracks.data.get(country.getName()))));

        return instance.zipTopArtistAndTrackByCountryGuava(artistsByCountry, tracksByCountry);
    }

    public Seq<Triplet<Country, Artist, Track>> getJool() {
        Predicate<Country> isNonEnglishSpeaking = country -> Seq.seq(country.getLanguages())
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        Seq<Pair<Country, Seq<Artist>>> artistsByCountry = Seq.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Seq.of(instance.artists.data.get(country.getName()))));

        Seq<Pair<Country, Seq<Track>>> tracksByCountry = Seq.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Seq.of(instance.tracks.data.get(country.getName()))));

        return instance.zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry);
    }

    public Sequence<Triplet<Country, Artist, Track>> getKotlin() {
        return ZipTopArtistAndTrackByCountryKt.zipTopArtistAndTrackByCountry(ArtistsKt.artistsByCountry(), TracksKt.tracksByCountry());
    }

    public Sequence<Triplet<Country, Artist, Track>> getJKotlin() {
        Function1<Country, Boolean> isNonEnglishSpeaking = country -> {
            return none(
                    map(
                            CollectionsKt.asSequence(country.getLanguages()),
                            Language::getIso6391
                    ),
                    ENGLISH.getLanguage()::equals
            );
        };

        Sequence<Pair<Country, Sequence<Artist>>> artistsByCountry = map(
                filter(
                        filter(
                                asSequence(instance.countries.data),
                                isNonEnglishSpeaking
                        ),
                        country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0
                ),
                country -> Pair.with(country, asSequence(instance.artists.data.get(country.getName())))
        );

        Sequence<Pair<Country, Sequence<Track>>> tracksByCountry = map(
                filter(
                        filter(
                                asSequence(instance.countries.data),
                                isNonEnglishSpeaking
                        ),
                        country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0
                ),
                country -> Pair.with(country, asSequence(instance.tracks.data.get(country.getName())))
        );

        return instance.zipTopArtistAndTrackByCountry(artistsByCountry, tracksByCountry);
    }
}

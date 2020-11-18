package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.kt.zip.ArtistsInTopTenWithTopTenTracksByCountryKt;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ArtistsKt;
import com.github.tiniyield.sequences.benchmarks.kt.zip.TracksKt;
import com.github.tiniyield.sequences.benchmarks.common.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.common.model.country.Language;
import com.github.tiniyield.sequences.benchmarks.common.model.track.Track;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import one.util.streamex.StreamEx;
import org.javatuples.Pair;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Locale.ENGLISH;
import static org.testng.Assert.assertTrue;

public class ArtistsInTopTenWithTopTenTracksByCountryBenchmarkTest {


    private ArtistsInTopTenWithTopTenTracksByCountryBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new ArtistsInTopTenWithTopTenTracksByCountryBenchmark();
        instance.setup();
    }

    @Test
    public void testSameResultsArtistsInTopTenWithTopTenTracksByCountry() {
        List<Pair<Country, List<Artist>>> stream = getStream().collect(Collectors.toList());
        List<Pair<Country, List<Artist>>> streamEx = getStreamEx().toList();
        List<Pair<Country, List<Artist>>> query = getQuery().toList();
        List<Pair<Country, List<Artist>>> jool = getJool().toList();
        List<Pair<Country, List<Artist>>> vavr = getVavr().toJavaList();
        List<Pair<Country, List<Artist>>> protonpack = getProtonpack().collect(Collectors.toList());
        List<Pair<Country, List<Artist>>> guava = getGuava().collect(Collectors.toList());
        List<Pair<Country, List<Artist>>> kotlin = SequencesKt.toList(getKotlin());
        List<Pair<Country, List<Artist>>> jKotlin = SequencesKt.toList(getJKotlin());
        List<Pair<Country, List<Artist>>> zipline = getZipline().collect(Collectors.toList());

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
            Pair<Country, List<Artist>> a = stream.get(i);
            Pair<Country, List<Artist>> b = kotlin.get(i);
            assertTrue(
                    a.getValue0().equals(b.getValue0()) &&
                            a.getValue1().equals(b.getValue1())
            );
        }
    }


    public io.vavr.collection.Stream<Pair<Country, List<Artist>>> getVavr() {
        Predicate<Country> isNonEnglishSpeaking = country -> !io.vavr.collection.Stream.ofAll(country.getLanguages())
                .map(Language::getIso6391)
                .exists(ENGLISH.getLanguage()::equals);

        io.vavr.collection.Stream<Pair<Country, io.vavr.collection.Stream<Artist>>> artistsByCountry =
                io.vavr.collection.Stream.of(instance.countries.data)
                        .filter(isNonEnglishSpeaking)
                        .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                        .map(country -> Pair.with(country, io.vavr.collection.Stream.of(instance.artists.data.get(country.getName()))));

        io.vavr.collection.Stream<Pair<Country, io.vavr.collection.Stream<Track>>> tracksByCountry =
                io.vavr.collection.Stream.of(instance.countries.data)
                        .filter(isNonEnglishSpeaking)
                        .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                        .map(country -> Pair.with(country, io.vavr.collection.Stream.of(instance.tracks.data.get(country.getName()))));


        return instance.artistsInTopTenWithTopTenTracksByCountry(artistsByCountry, tracksByCountry);
    }

    public StreamEx<Pair<Country, List<Artist>>> getStreamEx() {
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


        return instance.artistsInTopTenWithTopTenTracksByCountry(artistsByCountry, tracksByCountry);
    }

    public Stream<Pair<Country, List<Artist>>> getZipline() {
        Predicate<Country> isNonEnglishSpeaking = country -> country.getLanguages().stream()
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        Stream<Pair<Country, Stream<Artist>>> artistsByCountry = Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(instance.artists.data.get(country.getName()))));

        Stream<Pair<Country, Stream<Track>>> tracksByCountry = Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(instance.tracks.data.get(country.getName()))));

        return instance.artistsInTopTenWithTopTenTracksByCountryZipline(artistsByCountry, tracksByCountry);
    }

    public Stream<Pair<Country, List<Artist>>> getProtonpack() {
        Predicate<Country> isNonEnglishSpeaking = country -> country.getLanguages().stream()
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        Stream<Pair<Country, Stream<Artist>>> artistsByCountry = Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(instance.artists.data.get(country.getName()))));

        Stream<Pair<Country, Stream<Track>>> tracksByCountry = Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(instance.tracks.data.get(country.getName()))));

        return instance.artistsInTopTenWithTopTenTracksByCountryProtonpack(artistsByCountry, tracksByCountry);
    }

    public Stream<Pair<Country, List<Artist>>> getStream() {
        Predicate<Country> isNonEnglishSpeaking = country -> country.getLanguages().stream()
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        Stream<Pair<Country, Stream<Artist>>> artistsByCountry = Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(instance.artists.data.get(country.getName()))));

        Stream<Pair<Country, Stream<Track>>> tracksByCountry = Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(instance.tracks.data.get(country.getName()))));

        return instance.artistsInTopTenWithTopTenTracksByCountry(artistsByCountry, tracksByCountry);
    }

    public Query<Pair<Country, List<Artist>>> getQuery() {
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

        return instance.artistsInTopTenWithTopTenTracksByCountry(artistsByCountry, tracksByCountry);
    }

    public Stream<Pair<Country, List<Artist>>> getGuava() {
        Predicate<Country> isNonEnglishSpeaking = country -> country.getLanguages().stream()
                .map(Language::getIso6391)
                .noneMatch(ENGLISH.getLanguage()::equals);

        Stream<Pair<Country, Stream<Artist>>> artistsByCountry = Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(instance.artists.data.get(country.getName()))));

        Stream<Pair<Country, Stream<Track>>> tracksByCountry = Stream.of(instance.countries.data)
                .filter(isNonEnglishSpeaking)
                .filter(country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0)
                .map(country -> Pair.with(country, Stream.of(instance.tracks.data.get(country.getName()))));

        return instance.artistsInTopTenWithTopTenTracksByCountryGuava(artistsByCountry, tracksByCountry);
    }

    public Seq<Pair<Country, List<Artist>>> getJool() {
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

        return instance.artistsInTopTenWithTopTenTracksByCountry(artistsByCountry, tracksByCountry);
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
                                ArraysKt.asSequence(instance.countries.data),
                                isNonEnglishSpeaking
                        ),
                        country -> instance.artists.data.containsKey(country.getName()) && instance.artists.data.get(country.getName()).length > 0
                ),
                country -> Pair.with(country, ArraysKt.asSequence(instance.artists.data.get(country.getName())))
        );

        Sequence<Pair<Country, Sequence<Track>>> tracksByCountry = SequencesKt.map(
                SequencesKt.filter(
                        SequencesKt.filter(
                                ArraysKt.asSequence(instance.countries.data),
                                isNonEnglishSpeaking
                        ),
                        country -> instance.tracks.data.containsKey(country.getName()) && instance.tracks.data.get(country.getName()).length > 0
                ),
                country -> Pair.with(country, ArraysKt.asSequence(instance.tracks.data.get(country.getName())))
        );

        return instance.artistsInTopTenWithTopTenTracksByCountry(artistsByCountry, tracksByCountry);
    }

}

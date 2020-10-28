package com.github.tiniyield.sequences.benchmarks.operations.utils;


import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Language;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.*;
import static java.util.Locale.ENGLISH;

public class SequenceBenchmarkVavrUtils {


    public static Stream<Pair<Country, Stream<Track>>> getTracks() {
        return COUNTRY_DATA.asVavrStream()
                           .filter(SequenceBenchmarkVavrUtils::isNonEnglishSpeaking)
                           .filter(country -> TRACKS_DATA.hasDataForCountry(country.getName()))
                           .map(country -> Pair.with(country, TRACKS_DATA.asVavrStream(country.getName())));
    }

    public static Stream<Pair<Country, Stream<Artist>>> getArtists() {
        return COUNTRY_DATA.asVavrStream()
                           .filter(SequenceBenchmarkVavrUtils::isNonEnglishSpeaking)
                           .filter(country -> ARTISTS_DATA.hasDataForCountry(country.getName()))
                           .map(country -> Pair.with(country, ARTISTS_DATA.asVavrStream(country.getName())));
    }

    public static boolean isNonEnglishSpeaking(Country country) {
        return !Stream.ofAll(country.getLanguages())
                      .map(Language::getIso6391)
                      .exists(ENGLISH.getLanguage()::equals);
    }

    public static final Function<Tuple2<Pair<Country, Stream<Artist>>, Pair<Country, Stream<Track>>>, Triplet<Country,
                    Stream<Artist>, Stream<Track>>> TO_DATA_TRIPLET_BY_COUNTRY = entry -> {
        Pair<Country, Stream<Artist>> key = entry._1();
        return Triplet.with(
                key.getValue0(),
                key.getValue1(),
                entry._2().getValue1());
    };

    public static final Function<Tuple2<Pair<Country, Stream<Artist>>, Pair<Country, Stream<Track>>>,
            Triplet<Country, Artist,
            Track>> TO_TOP_BY_COUNTRY_TRIPLET = entry -> {
        Pair<Country, Stream<Artist>> key = entry._1();
        return Triplet.with(
                key.getValue0(),
                key.getValue1().getOrNull(),
                entry._2().getValue1().getOrNull());
    };

    public static final Function<Triplet<Country, Stream<Artist>, Stream<Track>>, Pair<Country, java.util.List<Artist>>> TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY = triplet -> {
        List<String> topTenSongsArtistsNames = triplet.getValue2()
                                                      .take(TEN)
                                                      .map(Track::getArtist)
                                                      .map(Artist::getName)
                                                      .toList();

        java.util.List<Artist> artists = triplet.getValue1()
                                      .take(TEN)
                                      .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                                      .collect(Collectors.toList());
        return Pair.with(triplet.getValue0(), artists);

    };
}

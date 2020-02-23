package com.github.tiniyield.jayield.benchmark.query;

import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.ARTISTS_DATA;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.COUNTRY_DATA;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.TEN;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.TRACKS_DATA;
import static java.util.Locale.ENGLISH;


import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jayield.Query;

import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.country.Language;
import com.github.tiniyield.jayield.benchmark.model.track.Track;

public class SequenceBenchmarkQueryUtils {


    public static Query<Pair<Country, Query<Track>>> getTracks() {
        return COUNTRY_DATA.asQuery()
                           .filter(SequenceBenchmarkQueryUtils::isNonEnglishSpeaking)
                           .filter(country -> TRACKS_DATA.hasDataForCountry(country.getName()))
                           .map(country -> Pair.with(country, TRACKS_DATA.asQuery(country.getName())));
    }

    public static Query<Pair<Country, Query<Artist>>> getArtists() {
        return COUNTRY_DATA.asQuery()
                           .filter(SequenceBenchmarkQueryUtils::isNonEnglishSpeaking)
                           .filter(country -> ARTISTS_DATA.hasDataForCountry(country.getName()))
                           .map(country -> Pair.with(country, ARTISTS_DATA.asQuery(country.getName())));
    }

    public static boolean isNonEnglishSpeaking(Country country) {
        return !Query.fromList(country.getLanguages())
                     .map(Language::getIso6391)
                     .anyMatch(ENGLISH.getLanguage()::equals);
    }

    public static final BiFunction<Pair<Country, Query<Artist>>, Pair<Country, Query<Track>>, Triplet<Country,
            Query<Artist>, Query<Track>>> TO_DATA_TRIPLET_BY_COUNTRY = (l, r) -> Triplet.with(
            l.getValue0(),
            l.getValue1(),
            r.getValue1());

    public static final BiFunction<Pair<Country, Query<Artist>>, Pair<Country, Query<Track>>, Triplet<Country,
            Artist, Track>> TO_TOP_BY_COUNTRY_TRIPLET = (l, r) -> Triplet.with(
            l.getValue0(),
            l.getValue1().findFirst().orElse(null),
            r.getValue1().findFirst().orElse(null));

    public static final Function<Triplet<Country, Query<Artist>, Query<Track>>, Pair<Country, List<Artist>>> TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY = triplet -> {
        List<String> topTenSongsArtistsNames = triplet.getValue2()
                                                      .limit(TEN)
                                                      .map(Track::getArtist)
                                                      .map(Artist::getName)
                                                      .toList();

        List<Artist> artists = triplet.getValue1()
                                      .limit(TEN)
                                      .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                                      .toList();
        return Pair.with(triplet.getValue0(), artists);

    };
}

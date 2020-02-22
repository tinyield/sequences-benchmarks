package com.github.tiniyield.jayield.benchmark.query;

import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.ARTISTS_DATA;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.COUNTRY_DATA;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.TRACKS_DATA;
import static java.util.Locale.ENGLISH;


import org.javatuples.Pair;
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
}

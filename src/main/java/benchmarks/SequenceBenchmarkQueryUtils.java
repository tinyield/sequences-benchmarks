package benchmarks;

import static benchmarks.SequenceBenchmarkConstants.ARTISTS_QUERY;
import static benchmarks.SequenceBenchmarkConstants.COUNTRY_QUERY;
//import static benchmarks.SequenceBenchmarkConstants.ENGLISH;
import static benchmarks.SequenceBenchmarkConstants.TRACKS_QUERY;
import static java.util.Locale.ENGLISH;


import org.javatuples.Pair;
import org.jayield.Query;

import model.artist.Artist;
import model.country.Country;
import model.country.Language;
import model.track.Track;

public class SequenceBenchmarkQueryUtils {


    static Query<Pair<Country, Query<Track>>> getTracks() {
        return COUNTRY_QUERY.getCountriesAsQuery()
                            .filter(SequenceBenchmarkQueryUtils::isNonEnglishSpeaking)
                            .filter(country -> TRACKS_QUERY.hasTracksForCountry(country.getName()))
                            .map(country -> Pair.with(country, TRACKS_QUERY.getTracksAsQuery(country.getName())));
    }

    static Query<Pair<Country, Query<Artist>>> getArtists() {
        return COUNTRY_QUERY.getCountriesAsQuery()
                            .filter(SequenceBenchmarkQueryUtils::isNonEnglishSpeaking)
                            .filter(country -> ARTISTS_QUERY.hasArtistsForCountry(country.getName()))
                            .map(country -> Pair.with(country, ARTISTS_QUERY.getArtistsAsQuery(country.getName())));
    }

    static boolean isNonEnglishSpeaking(Country country) {
        return !Query.fromList(country.getLanguages())
                     .map(Language::getIso6391)
                     .anyMatch(ENGLISH.getLanguage()::equals);
    }
}

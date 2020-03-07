package com.github.tiniyield.jayield.benchmark.streamex;

import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.ARTISTS_DATA;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.COUNTRY_DATA;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.TRACKS_DATA;
import static java.util.Locale.ENGLISH;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.country.Language;
import com.github.tiniyield.jayield.benchmark.model.track.Track;

import one.util.streamex.StreamEx;

public class SequenceBenchmarkStreamExUtils {


    public static StreamEx<Pair<Country, Stream<Track>>> getTracks() {
        return COUNTRY_DATA.asStreamEx()
                           .filter(SequenceBenchmarkStreamExUtils::isNonEnglishSpeaking)
                           .filter(country -> TRACKS_DATA.hasDataForCountry(country.getName()))
                           .map(country -> Pair.with(country, TRACKS_DATA.asStreamEx(country.getName())));
    }

    public static StreamEx<Pair<Country, Stream<Artist>>> getArtists() {
        return COUNTRY_DATA.asStreamEx()
                           .filter(SequenceBenchmarkStreamExUtils::isNonEnglishSpeaking)
                           .filter(country -> ARTISTS_DATA.hasDataForCountry(country.getName()))
                           .map(country -> Pair.with(country, ARTISTS_DATA.asStreamEx(country.getName())));
    }

    public static boolean isNonEnglishSpeaking(Country country) {
        return country.getLanguages().stream()
                      .map(Language::getIso6391)
                      .noneMatch(ENGLISH.getLanguage()::equals);
    }

    public static final Function<Map.Entry<Pair<Country, Stream<Artist>>, Pair<Country, Stream<Track>>>, Triplet<Country,
            Stream<Artist>, Stream<Track>>> TO_DATA_TRIPLET_BY_COUNTRY = entry -> {
        Pair<Country, Stream<Artist>> key = entry.getKey();
        return Triplet.with(
                key.getValue0(),
                key.getValue1(),
                entry.getValue().getValue1());
    };

    public static final Function<Map.Entry<Pair<Country, Stream<Artist>>, Pair<Country, Stream<Track>>>,
            Triplet<Country, Artist,
            Track>> TO_TOP_BY_COUNTRY_TRIPLET = entry -> {
        Pair<Country, Stream<Artist>> key = entry.getKey();
        return Triplet.with(
                key.getValue0(),
                key.getValue1().findFirst().orElse(null),
                entry.getValue().getValue1().findFirst().orElse(null));
    };
}

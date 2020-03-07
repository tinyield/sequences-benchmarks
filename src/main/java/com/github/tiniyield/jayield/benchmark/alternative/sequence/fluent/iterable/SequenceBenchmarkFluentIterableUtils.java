package com.github.tiniyield.jayield.benchmark.alternative.sequence.fluent.iterable;

import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.ARTISTS_DATA;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.COUNTRY_DATA;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.TEN;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.TRACKS_DATA;
import static java.util.Locale.ENGLISH;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.country.Language;
import com.github.tiniyield.jayield.benchmark.model.track.Track;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

public class SequenceBenchmarkFluentIterableUtils {


    public static FluentIterable<Pair<Country, FluentIterable<Track>>> getTracks() {
        return COUNTRY_DATA.asFluentIterable()
                           .filter(SequenceBenchmarkFluentIterableUtils::isNonEnglishSpeaking)
                           .filter(country -> TRACKS_DATA.hasDataForCountry(country.getName()))
                           .transform(country -> Pair.with(country, TRACKS_DATA.asFluentIterable(country.getName())));
    }

    public static FluentIterable<Pair<Country, FluentIterable<Artist>>> getArtists() {
        return COUNTRY_DATA.asFluentIterable()
                           .filter(SequenceBenchmarkFluentIterableUtils::isNonEnglishSpeaking)
                           .filter(country -> ARTISTS_DATA.hasDataForCountry(country.getName()))
                           .transform(country -> Pair.with(country, ARTISTS_DATA.asFluentIterable(country.getName())));
    }

    public static boolean isNonEnglishSpeaking(Country country) {
        return !FluentIterable.from(country.getLanguages())
                              .transform(Language::getIso6391)
                              .anyMatch(ENGLISH.getLanguage()::equals);
    }

    public static Function<Pair<Pair<Country, FluentIterable<Artist>>, Pair<Country, FluentIterable<Track>>>,
            Triplet<Country, FluentIterable<Artist>, FluentIterable<Track>>> TO_DATA_TRIPLET_BY_COUNTRY =
            value -> Triplet.with(value.getValue0().getValue0(),
                                  value.getValue0().getValue1(),
                                  value.getValue1().getValue1());


    public static Function<Pair<Pair<Country, FluentIterable<Artist>>, Pair<Country, FluentIterable<Track>>>,
            Triplet<Country, Artist, Track>> TO_TOP_BY_COUNTRY_TRIPLET =
            value -> {
                Pair<Country, FluentIterable<Artist>> countryArtists = value.getValue0();
                Pair<Country, FluentIterable<Track>> countryTracks = value.getValue1();
                return Triplet.with(
                        countryArtists.getValue0(),
                        countryArtists.getValue1().first().orNull(),
                        countryTracks.getValue1().first().orNull());
            };

    public static final Function<Triplet<Country, FluentIterable<Artist>, FluentIterable<Track>>,
            Pair<Country, List<Artist>>> TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY =
            triplet -> {
                List<String> topTenSongsArtistsNames = triplet.getValue2()
                                                              .limit(TEN)
                                                              .transform(Track::getArtist)
                                                              .transform(Artist::getName)
                                                              .toList();

                List<Artist> artists = triplet.getValue1()
                                              .limit(TEN)
                                              .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                                              .toList();
                return Pair.with(triplet.getValue0(), artists);

            };
}

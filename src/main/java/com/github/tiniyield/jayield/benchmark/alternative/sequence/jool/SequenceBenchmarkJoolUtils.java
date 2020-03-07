package com.github.tiniyield.jayield.benchmark.alternative.sequence.jool;

import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.ARTISTS_DATA;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.COUNTRY_DATA;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.TEN;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.TRACKS_DATA;
import static java.util.Locale.ENGLISH;

import java.util.List;
import java.util.function.Function;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;

import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.country.Language;
import com.github.tiniyield.jayield.benchmark.model.track.Track;

public class SequenceBenchmarkJoolUtils {


    public static Seq<Pair<Country, Seq<Track>>> getTracks() {
        return COUNTRY_DATA.asSeq()
                           .filter(SequenceBenchmarkJoolUtils::isNonEnglishSpeaking)
                           .filter(country -> TRACKS_DATA.hasDataForCountry(country.getName()))
                           .map(country -> Pair.with(country, TRACKS_DATA.asSeq(country.getName())));
    }

    public static Seq<Pair<Country, Seq<Artist>>> getArtists() {
        return COUNTRY_DATA.asSeq()
                           .filter(SequenceBenchmarkJoolUtils::isNonEnglishSpeaking)
                           .filter(country -> ARTISTS_DATA.hasDataForCountry(country.getName()))
                           .map(country -> Pair.with(country, ARTISTS_DATA.asSeq(country.getName())));
    }

    public static boolean isNonEnglishSpeaking(Country country) {
        return Seq.seq(country.getLanguages())
                  .map(Language::getIso6391)
                  .noneMatch(ENGLISH.getLanguage()::equals);
    }

    public static final Function<Tuple2<Pair<Country, Seq<Artist>>, Pair<Country, Seq<Track>>>, Triplet<Country,
            Seq<Artist>, Seq<Track>>> TO_DATA_TRIPLET_BY_COUNTRY = entry -> {
        Pair<Country, Seq<Artist>> key = entry.v1;
        return Triplet.with(
                key.getValue0(),
                key.getValue1(),
                entry.v2.getValue1());
    };

    public static final Function<Tuple2<Pair<Country, Seq<Artist>>, Pair<Country, Seq<Track>>>,
            Triplet<Country, Artist,
                    Track>> TO_TOP_BY_COUNTRY_TRIPLET = entry -> {
        Pair<Country, Seq<Artist>> key = entry.v1;
        return Triplet.with(
                key.getValue0(),
                key.getValue1().findFirst().orElse(null),
                entry.v2.getValue1().findFirst().orElse(null));
    };

    public static final Function<Triplet<Country, Seq<Artist>, Seq<Track>>, Pair<Country, List<Artist>>> TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY = triplet -> {
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

package com.github.tiniyield.jayield.benchmark.alternative.sequence.vavr.benchmark;

import static com.github.tiniyield.jayield.benchmark.alternative.sequence.vavr.SequenceBenchmarkVavrUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.vavr.SequenceBenchmarkVavrUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.vavr.SequenceBenchmarkVavrUtils.TO_TOP_BY_COUNTRY_TRIPLET;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.vavr.SequenceBenchmarkVavrUtils.getArtists;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.vavr.SequenceBenchmarkVavrUtils.getTracks;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.track.Track;

import io.vavr.collection.Stream;

public class VavrBenchmark {

    public static Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        return getArtists()
                .zip(getTracks())
                .map(TO_TOP_BY_COUNTRY_TRIPLET)
                .distinctBy(Triplet::getValue1);
    }

    public static Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        return getArtists()
                .zip(getTracks())
                .map(TO_DATA_TRIPLET_BY_COUNTRY)
                .map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }
}

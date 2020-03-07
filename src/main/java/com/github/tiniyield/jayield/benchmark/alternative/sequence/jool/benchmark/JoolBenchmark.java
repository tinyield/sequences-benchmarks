package com.github.tiniyield.jayield.benchmark.alternative.sequence.jool.benchmark;


import static com.github.tiniyield.jayield.benchmark.alternative.sequence.jool.SequenceBenchmarkJoolUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.jool.SequenceBenchmarkJoolUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.jool.SequenceBenchmarkJoolUtils.TO_TOP_BY_COUNTRY_TRIPLET;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.jool.SequenceBenchmarkJoolUtils.getArtists;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.jool.SequenceBenchmarkJoolUtils.getTracks;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jooq.lambda.Seq;

import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.track.Track;

public class JoolBenchmark {

    public static Seq<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        return getArtists()
                .zip(getTracks())
                .map(TO_TOP_BY_COUNTRY_TRIPLET)
                .distinct(Triplet::getValue1);
    }

    public static Seq<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        return getArtists()
                .zip(getTracks())
                .map(TO_DATA_TRIPLET_BY_COUNTRY)
                .map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }
}

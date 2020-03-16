package com.github.tiniyield.jayield.benchmark.alternative.sequence.streamex.benchmark;


import static com.github.tiniyield.jayield.benchmark.alternative.sequence.streamex.SequenceBenchmarkStreamExUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.streamex.SequenceBenchmarkStreamExUtils.TO_TOP_BY_COUNTRY_TRIPLET;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.streamex.SequenceBenchmarkStreamExUtils.getArtists;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.streamex.SequenceBenchmarkStreamExUtils.getTracks;
import static com.github.tiniyield.jayield.benchmark.stream.SequenceBenchmarkStreamUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.track.Track;

import one.util.streamex.StreamEx;

public class StreamExBenchmark {

    public static StreamEx<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        return getArtists()
                .zipWith(getTracks())
                .map(TO_TOP_BY_COUNTRY_TRIPLET)
                .distinct(Triplet::getValue1);
    }

    public static StreamEx<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        return getArtists()
                .zipWith(getTracks())
                .map(TO_DATA_TRIPLET_BY_COUNTRY)
                .map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }
}

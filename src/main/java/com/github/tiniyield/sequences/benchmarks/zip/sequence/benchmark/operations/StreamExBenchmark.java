package com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations;


import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.PRIME_NUMBERS_DATA_PROVIDER;
import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.VALUE_DATA_PROVIDER;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkStreamExUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkStreamExUtils.TO_TOP_BY_COUNTRY_TRIPLET;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkStreamExUtils.getArtists;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkStreamExUtils.getTracks;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkStreamUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;

import java.util.List;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.sequences.benchmarks.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.model.wrapper.Value;

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

    public static Stream<Pair<Integer, Value>> zipPrimeWithValue() {
        return PRIME_NUMBERS_DATA_PROVIDER.asStreamEx().zipWith(VALUE_DATA_PROVIDER.asStreamEx(), Pair::with);
    }
}

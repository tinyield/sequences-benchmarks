package com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations;

import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.PRIME_NUMBERS_DATA_PROVIDER;
import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.VALUE_DATA_PROVIDER;
import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkUtils.distinctByKey;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkQueryUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkQueryUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkQueryUtils.TO_TOP_BY_COUNTRY_TRIPLET;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkQueryUtils.getArtists;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkQueryUtils.getTracks;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jayield.Query;

import com.github.tiniyield.sequences.benchmarks.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.model.wrapper.Value;

public class QueryBenchmark {

    public static Query<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        return getArtists().zip(getTracks(), TO_TOP_BY_COUNTRY_TRIPLET)
                           .filter(distinctByKey(Triplet::getValue1));
    }

    public static Query<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        return getArtists().zip(getTracks(), TO_DATA_TRIPLET_BY_COUNTRY)
                           .map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public static Query<Pair<Integer, Value>> zipPrimeWithValue() {
        return PRIME_NUMBERS_DATA_PROVIDER.asQuery().zip(VALUE_DATA_PROVIDER.asQuery(), Pair::with);
    }
}

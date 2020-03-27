package com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations;


import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.PRIME_NUMBERS_DATA_PROVIDER;
import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.VALUE_DATA_PROVIDER;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jooq.lambda.Seq;

import com.github.tiniyield.sequences.benchmarks.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.model.wrapper.Value;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkJoolUtils;

public class JoolBenchmark {

    public static Seq<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        return SequenceBenchmarkJoolUtils.getArtists()
                                         .zip(SequenceBenchmarkJoolUtils.getTracks())
                                         .map(SequenceBenchmarkJoolUtils.TO_TOP_BY_COUNTRY_TRIPLET)
                                         .distinct(Triplet::getValue1);
    }

    public static Seq<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        return SequenceBenchmarkJoolUtils.getArtists()
                                         .zip(SequenceBenchmarkJoolUtils.getTracks())
                                         .map(SequenceBenchmarkJoolUtils.TO_DATA_TRIPLET_BY_COUNTRY)
                                         .map(SequenceBenchmarkJoolUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public static Seq<Pair<Integer, Value>> zipPrimeWithValue() {
        return PRIME_NUMBERS_DATA_PROVIDER.asSeq().zip(VALUE_DATA_PROVIDER.asSeq(), Pair::with);
    }
}

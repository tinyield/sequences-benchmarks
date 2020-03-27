package com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations;

import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.PRIME_NUMBERS_DATA_PROVIDER;
import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.VALUE_DATA_PROVIDER;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkStreamUtils.*;

import java.util.List;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.model.wrapper.Value;
import com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkStreamUtils;

public class StreamBenchmark {


    public static Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        return zip(
                getArtists(),
                getTracks(),
                TO_TOP_BY_COUNTRY_TRIPLET
        ).filter(SequenceBenchmarkUtils.distinctByKey(Triplet::getValue1));
    }

    public static Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        return zip(
                getArtists(),
                getTracks(),
                TO_DATA_TRIPLET_BY_COUNTRY
        ).map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public static Stream<Pair<Integer, Value>> zipPrimeWithValue() {
        return zip(
                PRIME_NUMBERS_DATA_PROVIDER.asStream(),
                VALUE_DATA_PROVIDER.asStream(),
                Pair::with
        );
    }
}

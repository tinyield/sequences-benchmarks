package com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations;

import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkUtils.getNumbersDataProvider;
import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkUtils.getValueDataProvider;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkStreamUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkStreamUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkStreamUtils.TO_TOP_BY_COUNTRY_TRIPLET;
import static com.google.common.collect.Streams.zip;

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

public class GuavaBenchmark {

    public static Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        return zip(
                SequenceBenchmarkStreamUtils.getArtists(),
                SequenceBenchmarkStreamUtils.getTracks(),
                TO_TOP_BY_COUNTRY_TRIPLET
        ).filter(SequenceBenchmarkUtils.distinctByKey(Triplet::getValue1));
    }

    public static Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        return zip(
                SequenceBenchmarkStreamUtils.getArtists(),
                SequenceBenchmarkStreamUtils.getTracks(),
                TO_DATA_TRIPLET_BY_COUNTRY
        ).map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public static Stream<Pair<Integer, Value>> zipPrimeWithValue() {
        return zip(
                getNumbersDataProvider().asStream().filter(SequenceBenchmarkUtils::isPrime),
                getValueDataProvider().asStream(),
                Pair::with
        );
    }
}

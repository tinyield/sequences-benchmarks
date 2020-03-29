package com.github.tiniyield.sequences.benchmarks.zip.sequence.benchmark.operations;


import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkUtils.getNumbersDataProvider;
import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkUtils.getValueDataProvider;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkStreamUtils.zip;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkVavrUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkVavrUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkVavrUtils.TO_TOP_BY_COUNTRY_TRIPLET;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkVavrUtils.getArtists;
import static com.github.tiniyield.sequences.benchmarks.zip.sequence.SequenceBenchmarkVavrUtils.getTracks;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.model.wrapper.Value;

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

    public static Stream<Pair<Integer, Value>> zipPrimeWithValue() {
        return getNumbersDataProvider().asVavrStream()
                                .filter(SequenceBenchmarkUtils::isPrime)
                                .zipWith(getValueDataProvider().asVavrStream(), Pair::with);
    }
}

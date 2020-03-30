package com.github.tiniyield.sequences.benchmarks.operations;


import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getEvenDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getNumbersDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getValueDataProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamExUtils;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils;

import one.util.streamex.StreamEx;

public class StreamExOperations {

    public static StreamEx<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        return SequenceBenchmarkStreamExUtils.getArtists()
                                             .zipWith(SequenceBenchmarkStreamExUtils.getTracks())
                                             .map(SequenceBenchmarkStreamExUtils.TO_TOP_BY_COUNTRY_TRIPLET)
                                             .distinct(Triplet::getValue1);
    }

    public static StreamEx<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        return SequenceBenchmarkStreamExUtils.getArtists()
                                             .zipWith(SequenceBenchmarkStreamExUtils.getTracks())
                                             .map(SequenceBenchmarkStreamExUtils.TO_DATA_TRIPLET_BY_COUNTRY)
                                             .map(SequenceBenchmarkStreamUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public static Stream<Pair<Integer, Value>> zipPrimeWithValue() {
        return getNumbersDataProvider().asStreamEx().filter(SequenceBenchmarkUtils::isPrime)
                                       .zipWith(
                                               getValueDataProvider().asStreamEx(),
                                               Pair::with);
    }

    public static boolean everyEven() {
        return getEvenDataProvider().asStreamEx()
                                    .allMatch(SequenceBenchmarkUtils::isEven);
    }

    public static Optional<Integer> find(AbstractBaseDataProvider<Integer> provider) {
        return provider.asStreamEx()
                       .filter(SequenceBenchmarkUtils::isOdd)
                       .findFirst();
    }
}

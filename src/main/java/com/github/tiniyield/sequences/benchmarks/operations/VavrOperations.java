package com.github.tiniyield.sequences.benchmarks.operations;


import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getEvenDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getNumbersDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getValueDataProvider;

import java.util.List;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkVavrUtils;

import io.vavr.collection.Stream;
import io.vavr.control.Option;

public class VavrOperations {

    public static Stream<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        return SequenceBenchmarkVavrUtils.getArtists()
                                         .zip(SequenceBenchmarkVavrUtils.getTracks())
                                         .map(SequenceBenchmarkVavrUtils.TO_TOP_BY_COUNTRY_TRIPLET)
                                         .distinctBy(Triplet::getValue1);
    }

    public static Stream<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        return SequenceBenchmarkVavrUtils.getArtists()
                                         .zip(SequenceBenchmarkVavrUtils.getTracks())
                                         .map(SequenceBenchmarkVavrUtils.TO_DATA_TRIPLET_BY_COUNTRY)
                                         .map(SequenceBenchmarkVavrUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public static Stream<Pair<Integer, Value>> zipPrimeWithValue() {
        return getNumbersDataProvider().asVavrStream()
                                       .filter(SequenceBenchmarkUtils::isPrime)
                                       .zipWith(getValueDataProvider().asVavrStream(), Pair::with);
    }

    public static boolean everyEven() {
        return getEvenDataProvider().asVavrStream()
                                    .forAll(SequenceBenchmarkUtils::isEven);
    }

    public static Option<Integer> find(AbstractBaseDataProvider<Integer> provider) {
        return provider.asVavrStream()
                       .find(SequenceBenchmarkUtils::isOdd);
    }
}

package com.github.tiniyield.sequences.benchmarks.operations;


import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getEvenDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getNumbersDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getValueDataProvider;

import java.util.List;
import java.util.Optional;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jooq.lambda.Seq;

import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkJoolUtils;

public class JoolOperations {

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
        return getNumbersDataProvider().asSeq()
                                       .filter(SequenceBenchmarkUtils::isPrime)
                                       .zip(getValueDataProvider().asSeq(), Pair::with);
    }

    public static boolean everyEven() {
        return getEvenDataProvider().asSeq()
                                    .allMatch(SequenceBenchmarkUtils::isEven);
    }

    public static Optional<Integer> find(AbstractBaseDataProvider<Integer> provider) {
        return provider.asSeq()
                       .filter(SequenceBenchmarkUtils::isOdd)
                       .findFirst();
    }
}

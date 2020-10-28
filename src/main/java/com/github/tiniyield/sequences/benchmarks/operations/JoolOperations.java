package com.github.tiniyield.sequences.benchmarks.operations;


import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jooq.lambda.Seq;

import java.util.List;

import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkJoolUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkJoolUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkJoolUtils.TO_TOP_BY_COUNTRY_TRIPLET;

public class JoolOperations {

    public Seq<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(Seq<Pair<Country, Seq<Artist>>> artists,
                                                                              Seq<Pair<Country, Seq<Track>>> tracks) {
        return artists.zip(tracks).map(TO_TOP_BY_COUNTRY_TRIPLET).distinct(Triplet::getValue1);
    }

    public Seq<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(Seq<Pair<Country, Seq<Artist>>> artists,
                                                                                     Seq<Pair<Country, Seq<Track>>> tracks) {
        return artists.zip(tracks)
                      .map(TO_DATA_TRIPLET_BY_COUNTRY)
                      .map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }


    public Seq<Pair<Integer, Value>> zipPrimeWithValue(Seq<Integer> numbers, Seq<Value> values) {
        return numbers.filter(SequenceBenchmarkUtils::isPrime).zip(values, Pair::with);
    }

}

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
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkJoolUtils.*;

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

    public Optional<Integer> findFirst(Seq<Integer> numbers) {
        return numbers.filter(SequenceBenchmarkUtils::isOdd).findFirst();
    }

    public <T> T find(Seq<T> q1, Seq<T> q2, BiPredicate<T, T> predicate) {
        return q1.zip(q2, (t1, t2) -> predicate.test(t1, t2) ? t1 : null)
                 .filter(Objects::nonNull)
                 .findFirst()
                 .orElse(null);
    }

}

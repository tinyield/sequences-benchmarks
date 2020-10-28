package com.github.tiniyield.sequences.benchmarks.operations;


import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils;
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import one.util.streamex.StreamEx;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import java.util.List;

import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamExUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamExUtils.TO_TOP_BY_COUNTRY_TRIPLET;
import static com.github.tiniyield.sequences.benchmarks.operations.utils.SequenceBenchmarkStreamUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;

public class StreamExOperations {

    public StreamEx<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry(
            StreamEx<Pair<Country, StreamEx<Artist>>> artists,
            StreamEx<Pair<Country, StreamEx<Track>>> tracks) {

        return artists.zipWith(tracks).map(TO_TOP_BY_COUNTRY_TRIPLET).distinct(Triplet::getValue1);
    }

    public StreamEx<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry(
            StreamEx<Pair<Country, StreamEx<Artist>>> artists,
            StreamEx<Pair<Country, StreamEx<Track>>> tracks) {

        return artists
                .zipWith(tracks)
                .map(TO_DATA_TRIPLET_BY_COUNTRY)
                .map(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }

    public StreamEx<Pair<Integer, Value>> zipPrimeWithValue(StreamEx<Integer> numbers, StreamEx<Value> values) {
        return numbers.filter(SequenceBenchmarkUtils::isPrime).zipWith(values, Pair::with);
    }

}

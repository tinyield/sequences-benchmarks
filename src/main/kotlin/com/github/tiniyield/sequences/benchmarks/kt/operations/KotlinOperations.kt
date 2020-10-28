package com.github.tiniyield.sequences.benchmarks.kt.operations

import com.github.tiniyield.sequences.benchmarks.kt.operations.utils.SequenceBenchmarkKtSequenceUtils
import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value
import org.javatuples.Pair
import org.javatuples.Triplet
import java.util.*

open class KotlinOperations {
    fun zipTopArtistAndTrackByCountry(artists: Sequence<Pair<Country, Sequence<Artist>>>,
                                      tracks: Sequence<Pair<Country, Sequence<Track>>>): Sequence<Triplet<Country, Artist, Track>> {
        return artists.zip(tracks)
                .map { pair -> SequenceBenchmarkKtSequenceUtils.TO_TOP_BY_COUNTRY_TRIPLET.apply(pair.first, pair.second) }
                .distinctBy { triplet -> triplet.getValue1() }
    }

    fun artistsInTopTenWithTopTenTracksByCountry(artists: Sequence<Pair<Country, Sequence<Artist>>>,
                                                 tracks: Sequence<Pair<Country, Sequence<Track>>>): Sequence<Pair<Country, List<Artist>>> {
        return artists.zip(tracks)
                .map { pair -> SequenceBenchmarkKtSequenceUtils.TO_DATA_TRIPLET_BY_COUNTRY.apply(pair.first, pair.second) }
                .map { triplet -> SequenceBenchmarkKtSequenceUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY.apply(triplet) }
    }

    fun zipPrimeWithValue(numbers: Sequence<Int>, values: Sequence<Value>): Sequence<Pair<Int, Value>> {
        return numbers.filter { value: Int -> SequenceBenchmarkUtils.isPrime(value) }
                .zip(values) { value0: Int, value1: Value -> Pair.with(value0, value1) }
    }

    fun findFirst(numbers: Sequence<Int?>): Optional<Int> {
        return Optional.ofNullable(numbers.find { value: Int? -> SequenceBenchmarkUtils.isOdd(value) })
    }

}

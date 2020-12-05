package com.github.tiniyield.sequences.benchmarks.kt.zip

import com.github.tiniyield.sequences.benchmarks.common.model.artist.Artist
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country
import com.github.tiniyield.sequences.benchmarks.common.model.track.Track
import org.javatuples.Triplet

/**
 * Takes two Kotlin {@link Sequence}s in Kotlin and zips them into a Trio of Country, First Artist and First Track,
 * and filters the resulting sequence by distinct Artists, using Protonpack
 * @return A Kotlin {@link Sequence} consisting of Trios of Country, First Artist and First Track
 * filtered to have only distinct artists
 */
fun zipTopArtistAndTrackByCountry(): Sequence<Triplet<Country, Artist, Track>> {
    return artistsByCountry().zip(tracksByCountry())
        .map { pair -> Triplet.with(pair.first.value0, pair.first.value1.first(), pair.second.value1.first()) }
        .distinctBy { triplet -> triplet.value1 }
}

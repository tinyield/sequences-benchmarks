package com.github.tiniyield.sequences.benchmarks.kt.zip

import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track
import org.javatuples.Pair
import org.javatuples.Triplet

fun zipTopArtistAndTrackByCountry(artists: Sequence<Pair<Country, Sequence<Artist>>>,
                                  tracks: Sequence<Pair<Country, Sequence<Track>>>): Sequence<Triplet<Country, Artist, Track>> {
    return artists.zip(tracks)
            .map { pair -> Triplet.with(pair.first.value0, pair.first.value1.first(), pair.second.value1.first()) }
            .distinctBy { triplet -> triplet.value1 }
}

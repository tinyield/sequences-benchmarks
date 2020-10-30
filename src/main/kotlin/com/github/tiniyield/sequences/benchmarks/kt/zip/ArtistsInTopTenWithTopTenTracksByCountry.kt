package com.github.tiniyield.sequences.benchmarks.kt.zip

import com.github.tiniyield.sequences.benchmarks.operations.common.BenchmarkConstants
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track
import org.javatuples.Pair
import org.javatuples.Triplet

fun artistsInTopTenWithTopTenTracksByCountry(artists: Sequence<Pair<Country, Sequence<Artist>>>,
                                             tracks: Sequence<Pair<Country, Sequence<Track>>>): Sequence<Pair<Country, List<Artist>>> {
    return artists.zip(tracks)
            .map { pair -> Triplet.with(pair.first.value0, pair.first.value1, pair.second.value1) }
            .map { triplet ->
                val topTenSongsArtistsNames = triplet.value2
                        .take(BenchmarkConstants.TEN)
                        .map { obj: Track -> obj.artist }
                        .map { obj: Artist -> obj.name }
                        .toList()
                val artists = triplet.value1
                        .take(BenchmarkConstants.TEN)
                        .filter { artist: Artist -> topTenSongsArtistsNames.contains(artist.name) }
                        .toList()
                Pair.with(triplet.value0, artists)
            }
}

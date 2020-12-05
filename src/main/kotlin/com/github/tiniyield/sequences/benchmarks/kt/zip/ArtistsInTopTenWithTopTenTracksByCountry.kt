package com.github.tiniyield.sequences.benchmarks.kt.zip

import com.github.tiniyield.sequences.benchmarks.common.model.artist.Artist
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country
import com.github.tiniyield.sequences.benchmarks.common.model.track.Track
import com.github.tiniyield.sequences.benchmarks.zip.ArtistsInTopTenWithTopTenTracksByCountryBenchmark
import org.javatuples.Pair
import org.javatuples.Triplet

/**
 * This method takes in two Kotlin {@link Sequence}s and for each Country, it takes the top ten Artists and the top ten Tracks
 * artist's names and zip them into a Trio. After that it filters the top ten artists by their presence in the
 * top ten Tracks artist's names, returning a Pair with the Country and the resulting Sequence of Artists
 *
 * @param artists sequence of artists by country
 * @param tracks sequence of tracks by country
 * @return A Kotlin {@link Sequence} in Kotlin consisting of Pairs of Country and Artists that are in that country's top ten and also
 * have tracks in the top ten of the same country
 */
fun artistsInTopTenWithTopTenTracksByCountry(): Sequence<Pair<Country, List<Artist>>> {
    return artistsByCountry().zip(tracksByCountry())
        .map { pair -> Triplet.with(pair.first.value0, pair.first.value1, pair.second.value1) }
        .map { triplet ->
            val topTenSongsArtistsNames = triplet.value2
                .take(ArtistsInTopTenWithTopTenTracksByCountryBenchmark.TEN)
                .map { obj: Track -> obj.artist }
                .map { obj: Artist -> obj.name }
                .toList()
            val artists = triplet.value1
                .take(ArtistsInTopTenWithTopTenTracksByCountryBenchmark.TEN)
                .filter { artist: Artist -> topTenSongsArtistsNames.contains(artist.name) }
                .toList()
            Pair.with(triplet.value0, artists)
        }
}

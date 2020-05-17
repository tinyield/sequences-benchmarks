package com.github.tiniyield.sequences.benchmarks.kt.operations.utils

import com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants
import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Language
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track
import org.javatuples.Pair
import org.javatuples.Triplet
import java.util.*
import java.util.function.BiFunction
import java.util.function.Function

object SequenceBenchmarkKtSequenceUtils {
    val tracks: Sequence<Pair<Country, Sequence<Track>>>
        get() = SequenceBenchmarkConstants.COUNTRY_DATA.asSequence()
                .filter { obj: Country -> isNonEnglishSpeaking(obj) }
                .filter { country: Country -> SequenceBenchmarkConstants.TRACKS_DATA.hasDataForCountry(country.name) }
                .map { country: Country -> Pair.with(country, SequenceBenchmarkConstants.TRACKS_DATA.asSequence(country.name)) }

    val artists: Sequence<Pair<Country, Sequence<Artist>>>
        get() = SequenceBenchmarkConstants.COUNTRY_DATA.asSequence()
                .filter { obj: Country -> isNonEnglishSpeaking(obj) }
                .filter { country: Country -> SequenceBenchmarkConstants.ARTISTS_DATA.hasDataForCountry(country.name) }
                .map { country: Country -> Pair.with(country, SequenceBenchmarkConstants.ARTISTS_DATA.asSequence(country.name)) }

    fun isNonEnglishSpeaking(country: Country): Boolean {
        return country.languages.asSequence()
                .map { obj: Language -> obj.iso6391 }
                .none { anObject: String -> Locale.ENGLISH.language.equals(anObject) }
    }


    val TO_DATA_TRIPLET_BY_COUNTRY = BiFunction { l: Pair<Country, Sequence<Artist>>, r: Pair<Country, Sequence<Track>> ->
        Triplet.with(
                l.value0,
                l.value1,
                r.value1)
    }

    val TO_TOP_BY_COUNTRY_TRIPLET = BiFunction { l: Pair<Country, Sequence<Artist>>, r: Pair<Country, Sequence<Track>> ->
        Triplet.with(
                l.value0,
                l.value1.first(),
                r.value1.first())
    }
    val TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY = Function { triplet: Triplet<Country, Sequence<Artist>, Sequence<Track>> ->
        val topTenSongsArtistsNames = triplet.value2
                .take(SequenceBenchmarkConstants.TEN)
                .map { obj: Track -> obj.artist }
                .map { obj: Artist -> obj.name }
                .toList()
        val artists = triplet.value1
                .take(SequenceBenchmarkConstants.TEN)
                .filter { artist: Artist -> topTenSongsArtistsNames.contains(artist.name) }
                .toList()
        Pair.with(triplet.value0, artists)
    }
}

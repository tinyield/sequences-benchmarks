package com.github.tiniyield.sequences.benchmarks.kt.zip

import com.github.tiniyield.sequences.benchmarks.common.data.providers.last.fm.Tracks
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country
import com.github.tiniyield.sequences.benchmarks.common.model.track.Track
import org.javatuples.Pair

private val provider = Tracks(countriesProvider)

fun tracksByCountry(): Sequence<Pair<Country, Sequence<Track>>> {
    return countries()
            .filter { obj: Country -> isNonEnglishSpeaking(obj) }
            .filter { country: Country -> provider.data.containsKey(country.name) && provider.data[country.name]!!.isNotEmpty() }
            .map { country: Country -> Pair.with(country, provider.data[country.name]!!.asSequence()) }
}

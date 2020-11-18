package com.github.tiniyield.sequences.benchmarks.kt.zip

import com.github.tiniyield.sequences.benchmarks.common.data.providers.last.fm.Artists
import com.github.tiniyield.sequences.benchmarks.common.model.artist.Artist
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country
import org.javatuples.Pair

private val provider = Artists(countriesProvider)

fun artistsByCountry(): Sequence<Pair<Country, Sequence<Artist>>> {
    return countries()
            .filter { obj: Country -> isNonEnglishSpeaking(obj) }
            .filter { country: Country -> provider.data.containsKey(country.name) && provider.data[country.name]!!.isNotEmpty() }
            .map { country: Country -> Pair.with(country, provider.data[country.name]!!.asSequence()) }
}

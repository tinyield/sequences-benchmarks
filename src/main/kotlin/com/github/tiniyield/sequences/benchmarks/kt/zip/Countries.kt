package com.github.tiniyield.sequences.benchmarks.kt.zip

import com.github.tiniyield.sequences.benchmarks.common.data.providers.rest.countries.Countries
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country

val countriesProvider = Countries()

fun countries(): Sequence<Country> {
    return countriesProvider.data.asSequence()
}

package com.github.tiniyield.sequences.benchmarks.kt.zip

import com.github.tiniyield.sequences.benchmarks.common.model.country.Country
import com.github.tiniyield.sequences.benchmarks.common.model.country.Language
import java.util.*

fun isNonEnglishSpeaking(country: Country): Boolean {
    return country.languages.asSequence()
            .map { obj: Language -> obj.iso6391 }
            .none { anObject: String -> Locale.ENGLISH.language.equals(anObject) }
}

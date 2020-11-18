package com.github.tiniyield.sequences.benchmarks.common.data.providers.rest.countries;

import com.github.tiniyield.sequences.benchmarks.common.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country;

public class Countries {

    public final Country[] data;

    public Countries() {
        data = new FileLoader().loadCountries().toArray(Country[]::new);
    }

}

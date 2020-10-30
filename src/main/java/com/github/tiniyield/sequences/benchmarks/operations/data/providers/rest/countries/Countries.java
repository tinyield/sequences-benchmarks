package com.github.tiniyield.sequences.benchmarks.operations.data.providers.rest.countries;

import com.github.tiniyield.sequences.benchmarks.operations.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;

public class Countries {

    public final Country[] data;

    public Countries() {
        data = new FileLoader().loadCountries().toArray(Country[]::new);
    }

}

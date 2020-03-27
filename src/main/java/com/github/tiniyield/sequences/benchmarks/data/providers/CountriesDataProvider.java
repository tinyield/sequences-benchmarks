package com.github.tiniyield.sequences.benchmarks.data.providers;

import com.github.tiniyield.sequences.benchmarks.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.model.country.Country;

public class CountriesDataProvider extends AbstractBaseDataProvider<Country> {

    private final Country[] data;

    public CountriesDataProvider() {
        data = new FileLoader().loadCountries().toArray(Country[]::new);
    }

    @Override
    protected Country[] data() {
        return data;
    }
}

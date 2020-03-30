package com.github.tiniyield.sequences.benchmarks.operations.data.providers.rest.countries;

import com.github.tiniyield.sequences.benchmarks.operations.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractBaseDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;

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

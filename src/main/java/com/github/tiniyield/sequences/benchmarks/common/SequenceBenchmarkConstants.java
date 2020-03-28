package com.github.tiniyield.sequences.benchmarks.common;

import com.github.tiniyield.sequences.benchmarks.data.providers.ArtistsDataProvider;
import com.github.tiniyield.sequences.benchmarks.data.providers.CountriesDataProvider;
import com.github.tiniyield.sequences.benchmarks.data.providers.PrimeNumbersDataProvider;
import com.github.tiniyield.sequences.benchmarks.data.providers.TracksDataProvider;
import com.github.tiniyield.sequences.benchmarks.data.providers.ValueDataProvider;

public class SequenceBenchmarkConstants {

    public static final CountriesDataProvider COUNTRY_DATA = new CountriesDataProvider();
    public static final ArtistsDataProvider ARTISTS_DATA = new ArtistsDataProvider(COUNTRY_DATA);
    public static final TracksDataProvider TRACKS_DATA = new TracksDataProvider(COUNTRY_DATA);
    public static final PrimeNumbersDataProvider PRIME_NUMBERS_DATA_PROVIDER = new PrimeNumbersDataProvider();
    public static final ValueDataProvider VALUE_DATA_PROVIDER = new ValueDataProvider();
    public static final int BENCHMARK_GENERATED_DATA_SIZE = 10_000;
    public static final boolean SILENT = true;
    public static final int TEN = 10;

}

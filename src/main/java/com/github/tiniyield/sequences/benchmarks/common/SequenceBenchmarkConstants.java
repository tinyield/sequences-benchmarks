package com.github.tiniyield.sequences.benchmarks.common;

import com.github.tiniyield.sequences.benchmarks.data.providers.ArtistsDataProvider;
import com.github.tiniyield.sequences.benchmarks.data.providers.CountriesDataProvider;
import com.github.tiniyield.sequences.benchmarks.data.providers.TracksDataProvider;

public class SequenceBenchmarkConstants {

    public static final CountriesDataProvider COUNTRY_DATA = new CountriesDataProvider();
    public static final ArtistsDataProvider ARTISTS_DATA = new ArtistsDataProvider(COUNTRY_DATA);
    public static final TracksDataProvider TRACKS_DATA = new TracksDataProvider(COUNTRY_DATA);
    public static final boolean SILENT = true;
    public static final int TEN = 10;

}

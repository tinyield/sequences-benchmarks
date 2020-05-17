package com.github.tiniyield.sequences.benchmarks.operations.common;

import com.github.tiniyield.sequences.benchmarks.operations.data.providers.last.fm.ArtistsDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.last.fm.TracksDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.rest.countries.CountriesDataProvider;

public class SequenceBenchmarkConstants {

    public static final CountriesDataProvider COUNTRY_DATA = new CountriesDataProvider();
    public static final ArtistsDataProvider ARTISTS_DATA = new ArtistsDataProvider(COUNTRY_DATA);
    public static final TracksDataProvider TRACKS_DATA = new TracksDataProvider(COUNTRY_DATA);
    public static final boolean SILENT = true;
    public static final int TEN = 10;
    public static final int ODD = 1;
    public static final int EVEN = 2;

}

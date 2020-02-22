package com.github.tiniyield.jayield.benchmark.common;

import com.github.tiniyield.jayield.benchmark.data.ArtistsData;
import com.github.tiniyield.jayield.benchmark.data.CountriesData;
import com.github.tiniyield.jayield.benchmark.data.TracksData;

public class SequenceBenchmarkConstants {

    public static final CountriesData COUNTRY_DATA = new CountriesData();
    public static final ArtistsData ARTISTS_DATA = new ArtistsData(COUNTRY_DATA);
    public static final TracksData TRACKS_DATA = new TracksData(COUNTRY_DATA);

}

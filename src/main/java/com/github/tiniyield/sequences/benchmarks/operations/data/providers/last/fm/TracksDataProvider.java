package com.github.tiniyield.sequences.benchmarks.operations.data.providers.last.fm;

import com.github.tiniyield.sequences.benchmarks.operations.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.AbstractCountryBasedDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.data.providers.rest.countries.CountriesDataProvider;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;

import java.util.HashMap;
import java.util.Map;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.SILENT;

public class TracksDataProvider extends AbstractCountryBasedDataProvider<Track> {
    private final Map<String, Track[]> data;

    public TracksDataProvider(CountriesDataProvider countries) {
        data = new HashMap<>();
        countries.asStream().forEach(country -> {
            String name = country.getName();
            Track[] countryTracks = new Track[0];
            try {
                countryTracks = new FileLoader().loadTracks(name);
            } catch (RuntimeException e) {
                if (!SILENT) {
                    System.out.println(String.format("data for country %s was ignored.", name));
                }
            }
            data.put(name, countryTracks);
        });
    }

    @Override
    protected Track[] data(String country) {
        return this.data.computeIfAbsent(country, (name) -> new Track[0]);
    }

}

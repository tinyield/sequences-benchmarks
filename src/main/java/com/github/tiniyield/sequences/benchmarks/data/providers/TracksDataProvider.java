package com.github.tiniyield.sequences.benchmarks.data.providers;

import static com.github.tiniyield.sequences.benchmarks.common.SequenceBenchmarkConstants.SILENT;

import java.util.HashMap;
import java.util.Map;

import com.github.tiniyield.sequences.benchmarks.data.loader.FileLoader;
import com.github.tiniyield.sequences.benchmarks.model.track.Track;

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

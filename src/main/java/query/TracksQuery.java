package query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.loader.FileLoader;
import model.track.Track;

public class TracksQuery {
    private final Map<String, Track[]> data;

    public TracksQuery(CountryQuery countries) {
        data = new HashMap<>();
        countries.getCountries().forEach(country -> {
            String name = country.getName();
            data.put(name, new FileLoader().loadTracks(name));
        });
    }

    public List<Track> getTracks(String country) {
        return Arrays.asList(getTracksForCountry(country));
    }

    private Track[] getTracksForCountry(String country) {
        return this.data.computeIfAbsent(country, (name) -> new Track[0]);
    }
}

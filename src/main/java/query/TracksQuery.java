package query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jayield.Query;

import data.loader.FileLoader;
import model.track.Track;

public class TracksQuery {
    private final Map<String, Track[]> data;

    public TracksQuery(CountryQuery countries) {
        data = new HashMap<>();
        countries.getCountriesAsStream().forEach(country -> {
            String name = country.getName();
            data.put(name, new FileLoader().loadTracks(name));
        });
    }

    public boolean hasTracksForCountry(String country) {
        return data.containsKey(country) && data.get(country).length > 0;
    }

    public List<Track> getTracks(String country) {
        return Arrays.asList(getTracksForCountry(country));
    }


    public Stream<Track> getTracksAsStream(String country) {
        return Stream.of(getTracksForCountry(country));
    }

    private Track[] getTracksForCountry(String country) {
        return this.data.computeIfAbsent(country, (name) -> new Track[0]);
    }

    public Query<Track> getTracksAsQuery(String country) {
        return Query.of(getTracksForCountry(country));
    }
}

package query;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import data.loader.FileLoader;
import model.artist.Artist;

public class ArtistsQuery {
    private final Map<String, Artist[]> data;

    public ArtistsQuery(CountryQuery countries) {
        data = new HashMap<>();
        countries.getCountries().forEach(country -> {
            String name = country.getName();
            data.put(name, new FileLoader().loadArtists(name));
        });

    }

    public List<Artist> getArtists(String country) {
        return Arrays.asList(getArtistsForCountry(country));
    }

    private Artist[] getArtistsForCountry(String country) {
        return this.data.computeIfAbsent(country, (name) -> new Artist[0]);
    }
}

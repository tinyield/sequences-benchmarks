package query;

import java.util.Arrays;
import java.util.stream.Stream;

import org.jayield.Query;

import data.loader.FileLoader;
import model.country.Country;

public class CountryQuery {

    private final Country[] data;

    public CountryQuery() {
        data = new FileLoader().loadCountries().toArray(Country[]::new);
    }

    public Stream<Country> getCountriesAsStream() {
        return Stream.of(data);
    }

    public Query<Country> getCountriesAsQuery() {
        return Query.of(data);
    }

    public Iterable<Country> getCountriesIterable() {
        return Arrays.asList(data);
    }
}

package com.github.tiniyield.jayield.benchmark.data.loader;

import static java.lang.String.format;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.github.tiniyield.jayield.benchmark.model.ApiKey;
import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.track.Track;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class FileLoader {

    private static final Type COUNTRY_REVIEW_TYPE = new TypeToken<List<Country>>() {
    }.getType();
    private static final Type ARTIST_REVIEW_TYPE = new TypeToken<List<Artist>>() {
    }.getType();
    private static final Type TRACK_REVIEW_TYPE = new TypeToken<List<Track>>() {
    }.getType();
    private static final String COUNTRIES_JSON = "countries.json";
    private static final String LASTFM_KEY = "lastfm.json";

    public Stream<Country> loadCountries() {
        return this.<Country>loadData(COUNTRIES_JSON, COUNTRY_REVIEW_TYPE).stream();
    }

    public ApiKey loadKey() {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(Objects.requireNonNull(getClass().getClassLoader()
                                                                                               .getResource(
                                                                                                       LASTFM_KEY))
                                                                     .getFile()));
            return gson.fromJson(reader, ApiKey.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public Artist[] loadArtists(String name) {
        return this.<Artist>loadData(format("artists/%s.json", name), ARTIST_REVIEW_TYPE)
                .toArray(Artist[]::new);
    }

    public Track[] loadTracks(String name) {
        return this.<Track>loadData(format("tracks/%s.json", name), TRACK_REVIEW_TYPE)
                .toArray(Track[]::new);
    }

    private <T> List<T> loadData(String file, Type type) {
        List<T> result = Collections.emptyList();
        try {
            Gson gson = new Gson();
            URL url = Objects.requireNonNull(getClass().getClassLoader().getResource(file));
            JsonReader reader = new JsonReader(new FileReader(url.getFile()));
            result = gson.fromJson(reader, type);

        } catch (FileNotFoundException | NullPointerException e) {
//            System.out.println(String.format("could not load data for file %s", file));
        }
        return result;
    }
}

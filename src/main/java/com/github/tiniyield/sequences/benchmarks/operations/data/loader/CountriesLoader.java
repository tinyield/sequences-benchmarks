package com.github.tiniyield.sequences.benchmarks.operations.data.loader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CountriesLoader {

    private static final Type COUNTRY_REVIEW_TYPE = new TypeToken<List<Country>>() {
    }.getType();
    private static final String COUNTRIES_JSON = "countries.json";

    public void fetch() {
        String webPage = "https://restcountries.eu/rest/v2/all";

        try (InputStream is = new URL(webPage).openStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            Gson gson = new Gson();
            List<Country> data = gson.fromJson(reader, COUNTRY_REVIEW_TYPE);

            FileWriter writer = new FileWriter(COUNTRIES_JSON);
            gson.toJson(data, writer);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

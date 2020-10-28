package com.github.tiniyield.sequences.benchmarks.operations.data.loader;

import com.github.tiniyield.sequences.benchmarks.operations.model.ApiKey;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.TopTrackLastFmResponse;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.google.gson.Gson;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.lang.String.format;

public class TracksLoader {

    private static final String TOP_TRACKS_BY_COUNTRY_QUERY_TEMPLATE = "method=geo.gettoptracks&country=%s&api_key" +
            "=%s&format=json";
    private static final String TRACKS_PATH_TEMPLATE = "tracks/%s.json";

    public void fetch(ApiKey key, String country) {
        try (InputStream is = new URI("http",
                                      "ws.audioscrobbler.com",
                                      "/2.0/",
                                      format(TOP_TRACKS_BY_COUNTRY_QUERY_TEMPLATE, country, key.getKey()),
                                      null
        ).toURL().openStream();
             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            Gson gson = new Gson();
            TopTrackLastFmResponse data = gson.fromJson(reader, TopTrackLastFmResponse.class);

            if(data != null && data.getTracks() != null && data.getTracks().getTrack().size() > 0) {
                List<Track> artists = data.getTracks().getTrack();
                FileWriter writer = new FileWriter(format(TRACKS_PATH_TEMPLATE, country));
                gson.toJson(artists, writer);
                writer.flush();
                writer.close();

            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

package data.loader;

import static java.lang.String.format;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.google.gson.Gson;

import model.ApiKey;
import model.artist.Artist;
import model.artist.LastFmResponse;
import model.track.TopTracks;
import model.track.Track;

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
            TopTracks data = gson.fromJson(reader, TopTracks.class);

            if(data != null && data.getTracks() != null && data.getTracks().getTrack().size() > 0) {
                List<Track> artists = data.getTracks().getTrack();
                FileWriter writer = new FileWriter(new Path().getPath(format(TRACKS_PATH_TEMPLATE, country)));
                gson.toJson(artists, writer);
                writer.flush();
                writer.close();

            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}

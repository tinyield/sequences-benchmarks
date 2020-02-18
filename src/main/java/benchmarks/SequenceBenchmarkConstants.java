package benchmarks;

import query.ArtistsQuery;
import query.CountryQuery;
import query.TracksQuery;

public class SequenceBenchmarkConstants {

    public static final CountryQuery COUNTRY_QUERY = new CountryQuery();
    public static final ArtistsQuery ARTISTS_QUERY = new ArtistsQuery(COUNTRY_QUERY);
    public static final TracksQuery TRACKS_QUERY = new TracksQuery(COUNTRY_QUERY);

}

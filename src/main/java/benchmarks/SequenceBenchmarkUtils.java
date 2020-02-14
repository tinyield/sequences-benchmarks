package benchmarks;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.javatuples.Pair;
import org.jayield.Query;

import model.artist.Artist;
import model.country.Country;
import model.country.Language;
import model.track.Track;
import query.ArtistsQuery;
import query.CountryQuery;
import query.TracksQuery;

public class SequenceBenchmarkUtils {

    private final String ENGLISH = "en";

    private final CountryQuery countryQuery;
    private final ArtistsQuery artistsQuery;
    private final TracksQuery tracksQuery;
    private Pair<Country, List<Track>>[] tracksByCountry;
    private Pair<Country, List<Artist>>[] artistsByCountry;

    public SequenceBenchmarkUtils() {
        countryQuery = new CountryQuery();
        artistsQuery = new ArtistsQuery(countryQuery);
        tracksQuery = new TracksQuery(countryQuery);
        tracksByCountry = countryQuery.getCountriesAsStream()
                                      .filter(this::isNonEnglishSpeaking)
                                      .filter(country -> tracksQuery.hasTracksForCountry(country.getName()))
                                      .map(country -> Pair.with(country, tracksQuery.getTracks(country.getName())))
                                      .collect(Collectors.toList())
                                      .toArray(Pair[]::new);
        artistsByCountry = countryQuery.getCountriesAsStream()
                                       .filter(this::isNonEnglishSpeaking)
                                       .filter(country -> artistsQuery.hasArtistsForCountry(country.getName()))
                                       .map(country -> Pair.with(country, artistsQuery.getArtists(country.getName())))
                                       .collect(Collectors.toList())
                                       .toArray(Pair[]::new);

    }

    List<Pair<Country, List<Track>>> getTracksByCountry() {
        return Arrays.asList(tracksByCountry);
    }

    Query<Pair<Country, List<Track>>> getTracksByCountryAsQuery() {
        return Query.of(tracksByCountry);
    }

    List<Pair<Country, List<Artist>>> getArtistsByCountry() {
        return Arrays.asList(artistsByCountry);
    }

    Query<Pair<Country, List<Artist>>> getArtistByCountryAsQuery() {
        return Query.of(artistsByCountry);

    }

    static <A, B, C> Stream<C> zip(Stream<? extends A> a,
                                   Stream<? extends B> b,
                                   BiFunction<? super A, ? super B, ? extends C> zipper) {
        Objects.requireNonNull(zipper);
        Spliterator<? extends A> aSpliterator = Objects.requireNonNull(a).spliterator();
        Spliterator<? extends B> bSpliterator = Objects.requireNonNull(b).spliterator();

        // Zipping looses DISTINCT and SORTED characteristics
        int characteristics = aSpliterator.characteristics() & bSpliterator.characteristics() &
                ~(Spliterator.DISTINCT | Spliterator.SORTED);

        long zipSize = ((characteristics & Spliterator.SIZED) != 0)
                ? Math.min(aSpliterator.getExactSizeIfKnown(), bSpliterator.getExactSizeIfKnown())
                : -1;

        Iterator<A> aIterator = Spliterators.iterator(aSpliterator);
        Iterator<B> bIterator = Spliterators.iterator(bSpliterator);
        Iterator<C> cIterator = new Iterator<C>() {
            @Override
            public boolean hasNext() {
                return aIterator.hasNext() && bIterator.hasNext();
            }

            @Override
            public C next() {
                return zipper.apply(aIterator.next(), bIterator.next());
            }
        };

        Spliterator<C> split = Spliterators.spliterator(cIterator, zipSize, characteristics);
        return (a.isParallel() || b.isParallel())
                ? StreamSupport.stream(split, true)
                : StreamSupport.stream(split, false);
    }


    static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    boolean isNonEnglishSpeaking(Country country) {
        return country.getLanguages().stream().map(Language::getIso6391).noneMatch(ENGLISH::equals);
    }
}

package benchmarks;

import static benchmarks.SequenceBenchmarkConstants.ARTISTS_QUERY;
import static benchmarks.SequenceBenchmarkConstants.COUNTRY_QUERY;
import static benchmarks.SequenceBenchmarkConstants.TRACKS_QUERY;
import static java.util.Locale.ENGLISH;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.javatuples.Pair;

import model.artist.Artist;
import model.country.Country;
import model.country.Language;
import model.track.Track;

public class SequenceBenchmarkStreamUtils {


    static Stream<Pair<Country, Stream<Track>>> getTracks() {
        return COUNTRY_QUERY.getCountriesAsStream()
                            .filter(SequenceBenchmarkStreamUtils::isNonEnglishSpeaking)
                            .filter(country -> TRACKS_QUERY.hasTracksForCountry(country.getName()))
                            .map(country -> Pair.with(country, TRACKS_QUERY.getTracksAsStream(country.getName())));
    }

    static Stream<Pair<Country, Stream<Artist>>> getArtists() {
        return COUNTRY_QUERY.getCountriesAsStream()
                            .filter(SequenceBenchmarkStreamUtils::isNonEnglishSpeaking)
                            .filter(country -> ARTISTS_QUERY.hasArtistsForCountry(country.getName()))
                            .map(country -> Pair.with(country, ARTISTS_QUERY.getArtistsAsStream(country.getName())));
    }

    static boolean isNonEnglishSpeaking(Country country) {
        return country.getLanguages().stream()
                      .map(Language::getIso6391)
                      .noneMatch(ENGLISH.getLanguage()::equals);
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
}

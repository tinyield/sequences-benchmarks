package com.github.tiniyield.jayield.benchmark.stream;

import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.ARTISTS_DATA;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.COUNTRY_DATA;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.TEN;
import static com.github.tiniyield.jayield.benchmark.common.SequenceBenchmarkConstants.TRACKS_DATA;
import static java.util.Locale.ENGLISH;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.country.Language;
import com.github.tiniyield.jayield.benchmark.model.track.Track;

public class SequenceBenchmarkStreamUtils {


    public static Stream<Pair<Country, Stream<Track>>> getTracks() {
        return COUNTRY_DATA.asStream()
                           .filter(SequenceBenchmarkStreamUtils::isNonEnglishSpeaking)
                           .filter(country -> TRACKS_DATA.hasDataForCountry(country.getName()))
                           .map(country -> Pair.with(country, TRACKS_DATA.asStream(country.getName())));
    }

    public static Stream<Pair<Country, Stream<Artist>>> getArtists() {
        return COUNTRY_DATA.asStream()
                           .filter(SequenceBenchmarkStreamUtils::isNonEnglishSpeaking)
                           .filter(country -> ARTISTS_DATA.hasDataForCountry(country.getName()))
                           .map(country -> Pair.with(country, ARTISTS_DATA.asStream(country.getName())));
    }

    public static boolean isNonEnglishSpeaking(Country country) {
        return country.getLanguages().stream()
                      .map(Language::getIso6391)
                      .noneMatch(ENGLISH.getLanguage()::equals);
    }

    public static <A, B, C> Stream<C> zip(Stream<? extends A> a,
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

    public static final BiFunction<Pair<Country, Stream<Artist>>, Pair<Country, Stream<Track>>, Triplet<Country,
            Stream<Artist>, Stream<Track>>> TO_DATA_TRIPLET_BY_COUNTRY = (l, r) -> Triplet.with(
            l.getValue0(),
            l.getValue1(),
            r.getValue1());

    public static final BiFunction<Pair<Country, Stream<Artist>>, Pair<Country, Stream<Track>>, Triplet<Country,
            Artist, Track>> TO_TOP_BY_COUNTRY_TRIPLET = (l, r) -> Triplet.with(
            l.getValue0(),
            l.getValue1().findFirst().orElse(null),
            r.getValue1().findFirst().orElse(null));

    public static final Function<Triplet<Country, Stream<Artist>, Stream<Track>>, Pair<Country, List<Artist>>> TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY = triplet -> {
        List<String> topTenSongsArtistsNames = triplet.getValue2()
                                                      .limit(TEN)
                                                      .map(Track::getArtist)
                                                      .map(Artist::getName)
                                                      .collect(Collectors.toList());

        List<Artist> artists = triplet.getValue1()
                                      .limit(TEN)
                                      .filter(artist -> topTenSongsArtistsNames.contains(artist.getName()))
                                      .collect(Collectors.toList());
        return Pair.with(triplet.getValue0(), artists);

    };
}

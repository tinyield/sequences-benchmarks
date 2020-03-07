package com.github.tiniyield.jayield.benchmark.alternative.sequence.fluent.iterable.benchmark;

import static com.github.tiniyield.jayield.benchmark.alternative.sequence.fluent.iterable.SequenceBenchmarkFluentIterableUtils.TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.fluent.iterable.SequenceBenchmarkFluentIterableUtils.TO_DATA_TRIPLET_BY_COUNTRY;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.fluent.iterable.SequenceBenchmarkFluentIterableUtils.TO_TOP_BY_COUNTRY_TRIPLET;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.fluent.iterable.SequenceBenchmarkFluentIterableUtils.getArtists;
import static com.github.tiniyield.jayield.benchmark.alternative.sequence.fluent.iterable.SequenceBenchmarkFluentIterableUtils.getTracks;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import com.github.tiniyield.jayield.benchmark.model.artist.Artist;
import com.github.tiniyield.jayield.benchmark.model.country.Country;
import com.github.tiniyield.jayield.benchmark.model.track.Track;
import com.google.common.collect.FluentIterable;


public class FluentIterableBenchmark {

    public static FluentIterable<Triplet<Country, Artist, Track>> zipTopArtistAndTrackByCountry() {
        Set<Artist> distinct = new HashSet<>();
        Iterator<Pair<Country, FluentIterable<Track>>> it = getTracks().iterator();
        return getArtists().transform(artists -> Pair.with(artists, it.next()))
                           .transform(TO_TOP_BY_COUNTRY_TRIPLET)
                           .filter(triplet -> distinct.add(triplet.getValue1()));
    }

    public static FluentIterable<Pair<Country, List<Artist>>> artistsInTopTenWithTopTenTracksByCountry() {
        Iterator<Pair<Country, FluentIterable<Track>>> it = getTracks().iterator();
        return getArtists().transform(artists -> Pair.with(artists, it.next()))
                           .transform(TO_DATA_TRIPLET_BY_COUNTRY)
                           .transform(TO_ARTISTS_IN_TOP_TEN_WITH_SONGS_IN_TOP_TEN_BY_COUNTRY);
    }
}

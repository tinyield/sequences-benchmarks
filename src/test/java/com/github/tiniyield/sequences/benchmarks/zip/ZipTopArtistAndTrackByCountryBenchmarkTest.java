package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.common.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.common.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ZipTopArtistAndTrackByCountryKt;
import kotlin.sequences.SequencesKt;
import org.javatuples.Triplet;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.tiniyield.sequences.benchmarks.zip.ZipTopArtistAndTrackByCountryBenchmark.eclipsePipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ZipTopArtistAndTrackByCountryBenchmark.guavaPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ZipTopArtistAndTrackByCountryBenchmark.jKotlinPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ZipTopArtistAndTrackByCountryBenchmark.jayieldPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ZipTopArtistAndTrackByCountryBenchmark.joolPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ZipTopArtistAndTrackByCountryBenchmark.protonpackPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ZipTopArtistAndTrackByCountryBenchmark.sekPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ZipTopArtistAndTrackByCountryBenchmark.streamExPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ZipTopArtistAndTrackByCountryBenchmark.streamPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ZipTopArtistAndTrackByCountryBenchmark.vavrPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ZipTopArtistAndTrackByCountryBenchmark.ziplinePipeline;
import static org.assertj.core.api.Assertions.assertThat;

public class ZipTopArtistAndTrackByCountryBenchmarkTest {

    static final int expected = 32;

    @Test
    public void testStreamPipeline() {
        List<Triplet<Country, Artist, Track>> actual = streamPipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        Set<Artist> artists = new HashSet<>();
        for (Triplet<Country, Artist, Track> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(artists.add(pair.getValue1())).isTrue();
        }
    }

    @Test
    public void testStreamExPipeline() {
        List<Triplet<Country, Artist, Track>> actual = streamExPipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        Set<Artist> artists = new HashSet<>();
        for (Triplet<Country, Artist, Track> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(artists.add(pair.getValue1())).isTrue();
        }
    }

    @Test
    public void testJayieldPipeline() {
        List<Triplet<Country, Artist, Track>> actual = jayieldPipeline().toList();

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        Set<Artist> artists = new HashSet<>();
        for (Triplet<Country, Artist, Track> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(artists.add(pair.getValue1())).isTrue();
        }
    }

    @Test
    public void testJoolPipeline() {
        List<Triplet<Country, Artist, Track>> actual = joolPipeline().toList();

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        Set<Artist> artists = new HashSet<>();
        for (Triplet<Country, Artist, Track> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(artists.add(pair.getValue1())).isTrue();
        }
    }

    @Test
    public void testVavrPipeline() {
        List<Triplet<Country, Artist, Track>> actual = vavrPipeline().toJavaList();

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        Set<Artist> artists = new HashSet<>();
        for (Triplet<Country, Artist, Track> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(artists.add(pair.getValue1())).isTrue();
        }
    }

    @Test
    public void testProtonpackPipeline() {
        List<Triplet<Country, Artist, Track>> actual = protonpackPipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        Set<Artist> artists = new HashSet<>();
        for (Triplet<Country, Artist, Track> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(artists.add(pair.getValue1())).isTrue();
        }
    }

    @Test
    public void testGuavaPipeline() {
        List<Triplet<Country, Artist, Track>> actual = guavaPipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        Set<Artist> artists = new HashSet<>();
        for (Triplet<Country, Artist, Track> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(artists.add(pair.getValue1())).isTrue();
        }
    }

    @Test
    public void testZiplinePipeline() {
        List<Triplet<Country, Artist, Track>> actual = ziplinePipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        Set<Artist> artists = new HashSet<>();
        for (Triplet<Country, Artist, Track> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(artists.add(pair.getValue1())).isTrue();
        }
    }

    @Test
    public void testKotlinPipeline() {
        List<Triplet<Country, Artist, Track>> actual = SequencesKt.toList(ZipTopArtistAndTrackByCountryKt.zipTopArtistAndTrackByCountry());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        Set<Artist> artists = new HashSet<>();
        for (Triplet<Country, Artist, Track> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(artists.add(pair.getValue1())).isTrue();
        }
    }

    @Test
    public void testJKotlinPipeline() {
        List<Triplet<Country, Artist, Track>> actual = SequencesKt.toList(jKotlinPipeline());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        Set<Artist> artists = new HashSet<>();
        for (Triplet<Country, Artist, Track> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(artists.add(pair.getValue1())).isTrue();
        }
    }

    @Test
    public void testEclipsePipeline() {
        List<Triplet<Country, Artist, Track>> actual = eclipsePipeline().toList();

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        Set<Artist> artists = new HashSet<>();
        for (Triplet<Country, Artist, Track> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(artists.add(pair.getValue1())).isTrue();
        }
    }

    @Test
    public void testSekPipeline() {
        List<Triplet<Country, Artist, Track>> actual = sekPipeline().toList();

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        Set<Artist> artists = new HashSet<>();
        for (Triplet<Country, Artist, Track> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(artists.add(pair.getValue1())).isTrue();
        }
    }
}

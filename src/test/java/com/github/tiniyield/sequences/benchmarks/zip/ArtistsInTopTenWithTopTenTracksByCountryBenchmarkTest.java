package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.common.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.common.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ArtistsInTopTenWithTopTenTracksByCountryKt;
import kotlin.sequences.SequencesKt;
import org.javatuples.Pair;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.tiniyield.sequences.benchmarks.zip.ArtistsInTopTenWithTopTenTracksByCountryBenchmark.eclipsePipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ArtistsInTopTenWithTopTenTracksByCountryBenchmark.guavaPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ArtistsInTopTenWithTopTenTracksByCountryBenchmark.jKotlinPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ArtistsInTopTenWithTopTenTracksByCountryBenchmark.jayieldPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ArtistsInTopTenWithTopTenTracksByCountryBenchmark.joolPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ArtistsInTopTenWithTopTenTracksByCountryBenchmark.protonpackPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ArtistsInTopTenWithTopTenTracksByCountryBenchmark.sekPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ArtistsInTopTenWithTopTenTracksByCountryBenchmark.streamExPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ArtistsInTopTenWithTopTenTracksByCountryBenchmark.streamPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ArtistsInTopTenWithTopTenTracksByCountryBenchmark.vavrPipeline;
import static com.github.tiniyield.sequences.benchmarks.zip.ArtistsInTopTenWithTopTenTracksByCountryBenchmark.ziplinePipeline;
import static org.assertj.core.api.Assertions.assertThat;

public class ArtistsInTopTenWithTopTenTracksByCountryBenchmarkTest {

    final int expected = 130;

    @Test
    public void testStreamPipeline() {
        List<Pair<Country, List<Artist>>> actual = streamPipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        for (Pair<Country, List<Artist>> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(pair.getValue1().size()).isLessThanOrEqualTo(10);
        }
    }

    @Test
    public void testStreamExPipeline() {
        List<Pair<Country, List<Artist>>> actual = streamExPipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        for (Pair<Country, List<Artist>> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(pair.getValue1().size()).isLessThanOrEqualTo(10);
        }
    }

    @Test
    public void testJayieldPipeline() {
        List<Pair<Country, List<Artist>>> actual = jayieldPipeline().toList();

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        for (Pair<Country, List<Artist>> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(pair.getValue1().size()).isLessThanOrEqualTo(10);
        }
    }

    @Test
    public void testJoolPipeline() {
        List<Pair<Country, List<Artist>>> actual = joolPipeline().toList();

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        for (Pair<Country, List<Artist>> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(pair.getValue1().size()).isLessThanOrEqualTo(10);
        }
    }

    @Test
    public void testVavrPipeline() {
        List<Pair<Country, List<Artist>>> actual = vavrPipeline().toJavaList();

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        for (Pair<Country, List<Artist>> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(pair.getValue1().size()).isLessThanOrEqualTo(10);
        }
    }

    @Test
    public void testProtonpackPipeline() {
        List<Pair<Country, List<Artist>>> actual = protonpackPipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        for (Pair<Country, List<Artist>> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(pair.getValue1().size()).isLessThanOrEqualTo(10);
        }
    }

    @Test
    public void testGuavaPipeline() {
        List<Pair<Country, List<Artist>>> actual = guavaPipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        for (Pair<Country, List<Artist>> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(pair.getValue1().size()).isLessThanOrEqualTo(10);
        }
    }

    @Test
    public void testZiplinePipeline() {
        List<Pair<Country, List<Artist>>> actual = ziplinePipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        for (Pair<Country, List<Artist>> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(pair.getValue1().size()).isLessThanOrEqualTo(10);
        }
    }

    @Test
    public void testKotlinPipeline() {
        List<Pair<Country, List<Artist>>> actual = SequencesKt.toList(ArtistsInTopTenWithTopTenTracksByCountryKt.artistsInTopTenWithTopTenTracksByCountry());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        for (Pair<Country, List<Artist>> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(pair.getValue1().size()).isLessThanOrEqualTo(10);
        }
    }

    @Test
    public void testJKotlinPipeline() {
        List<Pair<Country, List<Artist>>> actual = SequencesKt.toList(jKotlinPipeline());

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        for (Pair<Country, List<Artist>> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(pair.getValue1().size()).isLessThanOrEqualTo(10);
        }
    }

    @Test
    public void testEclipsePipeline() {
        List<Pair<Country, List<Artist>>> actual = eclipsePipeline().toList();

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        for (Pair<Country, List<Artist>> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(pair.getValue1().size()).isLessThanOrEqualTo(10);
        }
    }

    @Test
    public void testSekPipeline() {
        List<Pair<Country, List<Artist>>> actual = sekPipeline().toList();

        assertThat(actual.size()).isEqualTo(expected);
        Set<Country> countries = new HashSet<>();
        for (Pair<Country, List<Artist>> pair : actual) {
            assertThat(countries.add(pair.getValue0())).isTrue();
            assertThat(pair.getValue1().size()).isLessThanOrEqualTo(10);
        }
    }
}

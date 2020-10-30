package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import kotlin.sequences.SequencesKt;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class ArtistsInTopTenWithTopTenTracksByCountryBenchmarkTest {


    private ArtistsInTopTenWithTopTenTracksByCountryBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new ArtistsInTopTenWithTopTenTracksByCountryBenchmark();
        instance.setup();
    }

    @Test
    public void testSameResultsArtistsInTopTenWithTopTenTracksByCountry() {
        List<Pair<Country, List<Artist>>> stream = instance.getStream().collect(Collectors.toList());
        List<Pair<Country, List<Artist>>> streamEx = instance.getStreamEx().toList();
        List<Pair<Country, List<Artist>>> query = instance.getQuery().toList();
        List<Pair<Country, List<Artist>>> jool = instance.getJool().toList();
        List<Pair<Country, List<Artist>>> vavr = instance.getVavr().toJavaList();
        List<Pair<Country, List<Artist>>> protonpack = instance.getProtonpack().collect(Collectors.toList());
        List<Pair<Country, List<Artist>>> guava = instance.getGuava().collect(Collectors.toList());
        List<Pair<Country, List<Artist>>> kotlin = SequencesKt.toList(instance.getKotlin());
        List<Pair<Country, List<Artist>>> jKotlin = SequencesKt.toList(instance.getJKotlin());
        List<Pair<Country, List<Artist>>> zipline = instance.getZipline().collect(Collectors.toList());

        assertTrue(stream.size() == streamEx.size() && stream.containsAll(streamEx) && streamEx.containsAll(stream));
        assertTrue(stream.size() == query.size() && stream.containsAll(query) && query.containsAll(stream));
        assertTrue(stream.size() == jool.size() && stream.containsAll(jool) && jool.containsAll(stream));
        assertTrue(stream.size() == vavr.size() && stream.containsAll(vavr) && vavr.containsAll(stream));
        assertTrue(stream.size() == protonpack.size() && stream.containsAll(protonpack) && protonpack.containsAll(stream));
        assertTrue(stream.size() == guava.size() && stream.containsAll(guava) && guava.containsAll(stream));
        assertTrue(stream.size() == jKotlin.size() && stream.containsAll(jKotlin) && jKotlin.containsAll(stream));
        assertTrue(stream.size() == zipline.size() && stream.containsAll(zipline) && zipline.containsAll(stream));

        assertTrue(stream.size() == kotlin.size());
        for (int i = 0; i < stream.size(); i++) {
            Pair<Country, List<Artist>> a = stream.get(i);
            Pair<Country, List<Artist>> b = kotlin.get(i);
            assertTrue(
                    a.getValue0().equals(b.getValue0()) &&
                            a.getValue1().equals(b.getValue1())
            );
        }
    }

}

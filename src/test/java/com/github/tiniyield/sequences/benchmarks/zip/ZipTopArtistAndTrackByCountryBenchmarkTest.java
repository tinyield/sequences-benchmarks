package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import kotlin.sequences.SequencesKt;
import org.javatuples.Triplet;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.assertTrue;

public class ZipTopArtistAndTrackByCountryBenchmarkTest {

    private ZipTopArtistAndTrackByCountryBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new ZipTopArtistAndTrackByCountryBenchmark();
        instance.setup();
    }

    @Test
    public void testSameResultsZipTopArtistAndTrackByCountry() {
        List<Triplet<Country, Artist, Track>> stream = instance.getStream().collect(Collectors.toList());
        List<Triplet<Country, Artist, Track>> streamEx = instance.getStreamEx().toList();
        List<Triplet<Country, Artist, Track>> query = instance.getQuery().toList();
        List<Triplet<Country, Artist, Track>> jool = instance.getJool().toList();
        List<Triplet<Country, Artist, Track>> vavr = instance.getVavr().toJavaList();
        List<Triplet<Country, Artist, Track>> protonpack = instance.getProtonpack().collect(Collectors.toList());
        List<Triplet<Country, Artist, Track>> guava = instance.getGuava().collect(Collectors.toList());
        List<Triplet<Country, Artist, Track>> kotlin = SequencesKt.toList(instance.getKotlin());
        List<Triplet<Country, Artist, Track>> jKotlin = SequencesKt.toList(instance.getJKotlin());
        List<Triplet<Country, Artist, Track>> zipline = instance.getZipline().collect(Collectors.toList());

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
            Triplet<Country, Artist, Track> a = stream.get(i);
            Triplet<Country, Artist, Track> b = kotlin.get(i);
            assertTrue(
                    a.getValue0().equals(b.getValue0()) &&
                            a.getValue1().equals(b.getValue1()) &&
                            a.getValue2().equals(b.getValue2())
            );
        }
    }

}

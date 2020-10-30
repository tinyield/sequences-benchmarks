package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.operations.model.artist.Artist;
import com.github.tiniyield.sequences.benchmarks.operations.model.country.Country;
import com.github.tiniyield.sequences.benchmarks.operations.model.track.Track;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import kotlin.sequences.SequencesKt;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class VanillaZipBenchmarkTest {

    private VanillaZipBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new VanillaZipBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.init();
    }

    @Test
    public void testSameResultsVanillaZip() {
        List<Pair<Integer, Value>>  stream = instance.getStream().collect(Collectors.toList());
        List<Pair<Integer, Value>>  streamEx = instance.getStreamEx().toList();
        List<Pair<Integer, Value>>  query = instance.getQuery().toList();
        List<Pair<Integer, Value>>  jool = instance.getJool().toList();
        List<Pair<Integer, Value>>  vavr = instance.getVavr().toJavaList();
        List<Pair<Integer, Value>>  protonpack = instance.getProtonpack().collect(Collectors.toList());
        List<Pair<Integer, Value>>  guava = instance.getGuava().collect(Collectors.toList());
        List<Pair<Integer, Value>>  kotlin = SequencesKt.toList(instance.getKotlin());
        List<Pair<Integer, Value>>  jKotlin = SequencesKt.toList(instance.getJKotlin());
        List<Pair<Integer, Value>>  zipline = instance.getZipline().collect(Collectors.toList());

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
            Pair<Integer, Value> a = stream.get(i);
            Pair<Integer, Value> b = kotlin.get(i);
            assertTrue(
                    a.getValue0().equals(b.getValue0()) &&
                            a.getValue1().equals(b.getValue1())
            );
        }
    }
}

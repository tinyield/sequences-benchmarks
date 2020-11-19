package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.kt.zip.ZipPrimesWithValuesKt;
import com.github.tiniyield.sequences.benchmarks.common.model.wrapper.Value;
import one.util.streamex.StreamEx;
import org.eclipse.collections.api.factory.Lists;
import org.javatuples.Pair;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.sequences.SequencesKt.toList;
import static org.testng.Assert.assertTrue;

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
        List<Pair<Integer, Value>> stream = instance.zipPrimeWithValue(Arrays.stream(instance.numbers), Arrays.stream(instance.values)).collect(Collectors.toList());
        List<Pair<Integer, Value>> streamEx = instance.zipPrimeWithValue(StreamEx.of(instance.numbers), StreamEx.of(instance.values)).toList();
        List<Pair<Integer, Value>> query = instance.zipPrimeWithValue(Query.of(instance.numbers), Query.of(instance.values)).toList();
        List<Pair<Integer, Value>> jool = instance.zipPrimeWithValue(Seq.of(instance.numbers), Seq.of(instance.values)).toList();
        List<Pair<Integer, Value>> vavr = instance.zipPrimeWithValue(io.vavr.collection.Stream.of(instance.numbers), io.vavr.collection.Stream.of(instance.values)).toJavaList();
        List<Pair<Integer, Value>> protonpack = instance.zipPrimeWithValueProtonpack(Arrays.stream(instance.numbers), Arrays.stream(instance.values)).collect(Collectors.toList());
        List<Pair<Integer, Value>> guava = instance.zipPrimeWithValueGuava(Arrays.stream(instance.numbers), Arrays.stream(instance.values)).collect(Collectors.toList());
        List<Pair<Integer, Value>> kotlin = toList(ZipPrimesWithValuesKt.zipPrimeWithValue(asSequence(instance.numbers), asSequence(instance.values)));
        List<Pair<Integer, Value>> jKotlin = toList(instance.zipPrimeWithValue(asSequence(instance.numbers), asSequence(instance.values)));
        List<Pair<Integer, Value>> zipline = instance.zipPrimeWithValueZipline(Arrays.stream(instance.numbers), Arrays.stream(instance.values)).collect(Collectors.toList());
        List<Pair<Integer, Value>> eclipse = instance.zipPrimeWithValue(Lists.immutable.with(instance.numbers).asLazy(), Lists.immutable.with(instance.values).asLazy()).toList();

        assertTrue(stream.size() == streamEx.size() && stream.containsAll(streamEx) && streamEx.containsAll(stream));
        assertTrue(stream.size() == query.size() && stream.containsAll(query) && query.containsAll(stream));
        assertTrue(stream.size() == jool.size() && stream.containsAll(jool) && jool.containsAll(stream));
        assertTrue(stream.size() == vavr.size() && stream.containsAll(vavr) && vavr.containsAll(stream));
        assertTrue(stream.size() == protonpack.size() && stream.containsAll(protonpack) && protonpack.containsAll(stream));
        assertTrue(stream.size() == guava.size() && stream.containsAll(guava) && guava.containsAll(stream));
        assertTrue(stream.size() == jKotlin.size() && stream.containsAll(jKotlin) && jKotlin.containsAll(stream));
        assertTrue(stream.size() == zipline.size() && stream.containsAll(zipline) && zipline.containsAll(stream));
        assertTrue(stream.size() == eclipse.size() && stream.containsAll(eclipse) && eclipse.containsAll(stream));

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

package com.github.tiniyield.sequences.benchmarks.find;

import com.github.tiniyield.sequences.benchmarks.kt.find.FindKt;
import io.vavr.collection.Stream;
import kotlin.collections.ArraysKt;
import one.util.streamex.StreamEx;
import org.eclipse.collections.api.factory.Lists;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static kotlin.collections.CollectionsKt.asSequence;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class FindIntegerBenchmarkTest {


    private FindIntegerBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new FindIntegerBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.init();
        instance.update();
    }

    @Test
    public void testSameResultsFind() {
        Integer expected = 1;
        assertEquals(instance.find(instance.lstA.stream(), instance.lstB.stream(), Integer::equals), expected);
        assertEquals(instance.find(StreamEx.of(instance.lstA), StreamEx.of(instance.lstB), Integer::equals), expected);
        assertEquals(instance.find(Query.fromList(instance.lstA), Query.fromList(instance.lstB), Integer::equals), expected);
        assertEquals(instance.find(Seq.seq(instance.lstA), Seq.seq(instance.lstB), Integer::equals), expected);
        assertEquals(instance.findInProtonpack(instance.lstA.stream(), instance.lstB.stream(), Integer::equals), expected);
        assertEquals(instance.find(Stream.ofAll(instance.lstA), Stream.ofAll(instance.lstB), Integer::equals), expected);
        assertEquals(instance.findInGuava(instance.lstA.stream(), instance.lstB.stream(), Integer::equals), expected);
        assertEquals(instance.findInZipline(instance.lstA.stream(), instance.lstB.stream(), Integer::equals), expected);
        assertEquals(FindKt.find(asSequence(instance.lstA), asSequence(instance.lstB), Integer::equals), expected);
        assertEquals(instance.find(asSequence(instance.lstA), asSequence(instance.lstB), Integer::equals), expected);
        assertEquals(instance.find(Lists.immutable.ofAll(instance.lstA).asLazy(), Lists.immutable.ofAll(instance.lstB).asLazy(), Integer::equals), expected);
    }

    @Test
    public void testFindSuccess() {
        Integer expected = 1;
        Integer[] input = {1, 2, 3};
        assertEquals(instance.find(Arrays.stream(input), Arrays.stream(input), Integer::equals), expected);
        assertEquals(instance.find(StreamEx.of(input), StreamEx.of(input), Integer::equals), expected);
        assertEquals(instance.find(Query.of(input), Query.of(input), Integer::equals), expected);
        assertEquals(instance.find(Seq.of(input), Seq.of(input), Integer::equals), expected);
        assertEquals(instance.findInProtonpack(Arrays.stream(input), Arrays.stream(input), Integer::equals), expected);
        assertEquals(instance.find(Stream.of(input), Stream.of(input), Integer::equals), expected);
        assertEquals(instance.findInGuava(Arrays.stream(input), Arrays.stream(input), Integer::equals), expected);
        assertEquals(instance.findInZipline(Arrays.stream(input), Arrays.stream(input), Integer::equals), expected);
        assertEquals(FindKt.find(ArraysKt.asSequence(input), ArraysKt.asSequence(input), Integer::equals), expected);
        assertEquals(instance.find(ArraysKt.asSequence(input), ArraysKt.asSequence(input), Integer::equals), expected);
        assertEquals(instance.find(Lists.immutable.of(input).asLazy(), Lists.immutable.of(input).asLazy(), Integer::equals), expected);
    }


    @Test
    public void testFindFailure() {
        Integer[] input1 = {1, 2, 3};
        Integer[] input2 = {2, 3, 1};
        assertNull(instance.find(Arrays.stream(input1), Arrays.stream(input2), Integer::equals));
        assertNull(instance.find(StreamEx.of(input1), StreamEx.of(input2), Integer::equals));
        assertNull(instance.find(Query.of(input1), Query.of(input2), Integer::equals));
        assertNull(instance.find(Seq.of(input1), Seq.of(input2), Integer::equals));
        assertNull(instance.findInProtonpack(Arrays.stream(input1), Arrays.stream(input2), Integer::equals));
        assertNull(instance.find(Stream.of(input1), Stream.of(input2), Integer::equals));
        assertNull(instance.findInGuava(Arrays.stream(input1), Arrays.stream(input2), Integer::equals));
        assertNull(instance.findInZipline(Arrays.stream(input1), Arrays.stream(input2), Integer::equals));
        assertNull(FindKt.find(ArraysKt.asSequence(input1), ArraysKt.asSequence(input2), Integer::equals));
        assertNull(instance.find(ArraysKt.asSequence(input1), ArraysKt.asSequence(input2), Integer::equals));
        assertNull(instance.find(Lists.immutable.of(input1).asLazy(), Lists.immutable.of(input2).asLazy(), Integer::equals));
    }
}

package com.github.tiniyield.sequences.benchmarks.first;

import com.github.tiniyield.sequences.benchmarks.kt.first.FirstKt;
import io.vavr.collection.Stream;
import kotlin.collections.ArraysKt;
import one.util.streamex.StreamEx;
import org.eclipse.collections.api.factory.Lists;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.tinyield.Sek;

import java.util.Arrays;

import static com.github.tiniyield.sequences.benchmarks.common.BenchmarkConstants.EVEN;
import static com.github.tiniyield.sequences.benchmarks.common.BenchmarkConstants.ODD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class FindFirstInEndBenchmarkTest {

    private FindFirstInEndBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new FindFirstInEndBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.init();
    }

    @Test
    public void testSameResultsFind() {
        Integer expected = ODD;
        assertEquals(instance.findFirst(Arrays.stream(instance.data)), expected);
        assertEquals(instance.findFirst(StreamEx.of(instance.data)), expected);
        assertEquals(instance.findFirst(Query.of(instance.data)), expected);
        assertEquals(instance.findFirst(Seq.of(instance.data)), expected);
        assertEquals(instance.findFirst(io.vavr.collection.Stream.of(instance.data)), expected);
        assertEquals(FirstKt.findFirst(ArraysKt.asSequence(instance.data)), expected);
        assertEquals(instance.findFirst(ArraysKt.asSequence(instance.data)), expected);
        assertEquals(instance.findFirst(Lists.immutable.of(instance.data).asLazy()), expected);
        assertEquals(instance.findFirst(Sek.of(instance.data)), expected);
    }

    @Test
    public void testFindSuccess() {
        Integer expected = ODD;
        Integer[] input = {EVEN, ODD, EVEN};
        assertEquals(instance.findFirst(Arrays.stream(input)), expected);
        assertEquals(instance.findFirst(StreamEx.of(input)), expected);
        assertEquals(instance.findFirst(Query.of(input)), expected);
        assertEquals(instance.findFirst(Seq.of(input)), expected);
        assertEquals(instance.findFirst(Stream.of(input)), expected);
        assertEquals(FirstKt.findFirst(ArraysKt.asSequence(input)), expected);
        assertEquals(instance.findFirst(ArraysKt.asSequence(input)), expected);
        assertEquals(instance.findFirst(Lists.immutable.of(input).asLazy()), expected);
        assertEquals(instance.findFirst(Sek.of(input)), expected);
    }


    @Test
    public void testFindFailure() {
        Integer[] input = {EVEN, EVEN, EVEN};
        assertNull(instance.findFirst(Arrays.stream(input)));
        assertNull(instance.findFirst(StreamEx.of(input)));
        assertNull(instance.findFirst(Query.of(input)));
        assertNull(instance.findFirst(Seq.of(input)));
        assertNull(instance.findFirst(Stream.of(input)));
        assertNull(FirstKt.findFirst(ArraysKt.asSequence(input)));
        assertNull(instance.findFirst(ArraysKt.asSequence(input)));
        assertNull(instance.findFirst(Lists.immutable.of(input).asLazy()));
        assertNull(instance.findFirst(Sek.of(input)));
    }
}


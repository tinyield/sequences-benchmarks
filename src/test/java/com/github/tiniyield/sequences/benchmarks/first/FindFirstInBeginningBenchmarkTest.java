package com.github.tiniyield.sequences.benchmarks.first;

import com.github.tiniyield.sequences.benchmarks.kt.first.FirstKt;
import io.vavr.collection.Stream;
import kotlin.collections.ArraysKt;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.github.tiniyield.sequences.benchmarks.operations.common.BenchmarkConstants.EVEN;
import static com.github.tiniyield.sequences.benchmarks.operations.common.BenchmarkConstants.ODD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class FindFirstInBeginningBenchmarkTest {

    private FindFirstInBeginningBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new FindFirstInBeginningBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.init();
    }

    @Test
    public void testSameResultsFind() {
        Integer expected = ODD;
        assertEquals(instance.findFirstInStream(), expected);
        assertEquals(instance.findFirstInStreamEx(), expected);
        assertEquals(instance.findFirstInQuery(), expected);
        assertEquals(instance.findFirstInJool(), expected);
        assertEquals(instance.findFirstInVavr(), expected);
        assertEquals(instance.findFirstInKotlin(), expected);
        assertEquals(instance.findFirstInJKotlin(), expected);
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
    }
}


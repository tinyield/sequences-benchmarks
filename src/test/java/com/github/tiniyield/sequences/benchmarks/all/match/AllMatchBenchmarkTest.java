package com.github.tiniyield.sequences.benchmarks.all.match;

import com.github.tiniyield.sequences.benchmarks.kt.all.match.IsEveryEvenKt;
import kotlin.collections.ArraysKt;
import kotlin.sequences.SequencesKt;
import one.util.streamex.StreamEx;
import org.eclipse.collections.api.factory.Lists;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static kotlin.collections.ArraysKt.asSequence;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class AllMatchBenchmarkTest {

    private AllMatchBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new AllMatchBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.setup();
    }


    @Test
    public void testIsEveryEven() {
        assertTrue(instance.isEveryEven(Arrays.stream(instance.data)));
        assertTrue(instance.isEveryEven(StreamEx.of(instance.data)));
        assertTrue(instance.isEveryEven(Query.of(instance.data)));
        assertTrue(instance.isEveryEven(Seq.of(instance.data)));
        assertTrue(instance.isEveryEven(io.vavr.collection.Stream.of(instance.data)));
        assertTrue(IsEveryEvenKt.isEveryEven(asSequence(instance.data)));
        assertTrue(instance.isEveryEven(asSequence(instance.data)));
    }

    @Test
    public void testIsEveryEvenSuccess() {
        assertTrue(instance.isEveryEven(Stream.of(2,2,2)));
        assertTrue(instance.isEveryEven(StreamEx.of(2,2,2)));
        assertTrue(instance.isEveryEven(Query.of(2,2,2)));
        assertTrue(instance.isEveryEven(Seq.of(2,2,2)));
        assertTrue(instance.isEveryEven(io.vavr.collection.Stream.of(2,2,2)));
        assertTrue(IsEveryEvenKt.isEveryEven(asSequence(new int[]{2,2,2})));
        assertTrue(instance.isEveryEven(asSequence(new int[]{2,2,2})));
        assertTrue(instance.isEveryEven(Lists.immutable.of(2,2,2).asLazy()));
    }

    @Test
    public void testIsEveryEvenFailure() {
        assertFalse(instance.isEveryEven(Stream.of(2,1,2)));
        assertFalse(instance.isEveryEven(StreamEx.of(2,1,2)));
        assertFalse(instance.isEveryEven(Query.of(2,1,2)));
        assertFalse(instance.isEveryEven(Seq.of(2,1,2)));
        assertFalse(instance.isEveryEven(io.vavr.collection.Stream.of(2,1,2)));
        assertFalse(IsEveryEvenKt.isEveryEven(asSequence(new int[]{2,1,2})));
        assertFalse(instance.isEveryEven(asSequence(new int[]{2,1,2})));
        assertFalse(instance.isEveryEven(Lists.immutable.of(2,1,2).asLazy()));
    }
}

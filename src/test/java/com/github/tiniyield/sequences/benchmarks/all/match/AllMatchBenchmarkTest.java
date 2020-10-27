package com.github.tiniyield.sequences.benchmarks.all.match;

import com.github.tiniyield.sequences.benchmarks.kt.all.match.IsEveryEvenKt;
import kotlin.collections.ArraysKt;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.stream.Stream;

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
        assertTrue(instance.isEveryEvenStream());
        assertTrue(instance.isEveryEvenStreamEx());
        assertTrue(instance.isEveryEvenQuery());
        assertTrue(instance.isEveryEvenJool());
        assertTrue(instance.isEveryEvenVavr());
        assertTrue(instance.isEveryEvenKotlin());
        assertTrue(instance.isEveryEvenJKotlin());
    }

    @Test
    public void testIsEveryEvenSuccess() {
        assertTrue(instance.isEveryEven(Stream.of(2,2,2)));
        assertTrue(instance.isEveryEven(StreamEx.of(2,2,2)));
        assertTrue(instance.isEveryEven(Query.of(2,2,2)));
        assertTrue(instance.isEveryEven(Seq.of(2,2,2)));
        assertTrue(instance.isEveryEven(io.vavr.collection.Stream.of(2,2,2)));
        assertTrue(IsEveryEvenKt.isEveryEven(ArraysKt.asSequence(new int[]{2,2,2})));
        assertTrue(instance.isEveryEven(ArraysKt.asSequence(new int[]{2,2,2})));
    }

    @Test
    public void testIsEveryEvenFailure() {
        assertFalse(instance.isEveryEven(Stream.of(2,1,2)));
        assertFalse(instance.isEveryEven(StreamEx.of(2,1,2)));
        assertFalse(instance.isEveryEven(Query.of(2,1,2)));
        assertFalse(instance.isEveryEven(Seq.of(2,1,2)));
        assertFalse(instance.isEveryEven(io.vavr.collection.Stream.of(2,1,2)));
        assertFalse(IsEveryEvenKt.isEveryEven(ArraysKt.asSequence(new int[]{2,1,2})));
        assertFalse(instance.isEveryEven(ArraysKt.asSequence(new int[]{2,1,2})));
    }
}
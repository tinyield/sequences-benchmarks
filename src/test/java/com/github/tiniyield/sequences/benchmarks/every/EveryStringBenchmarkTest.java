package com.github.tiniyield.sequences.benchmarks.every;

import com.github.tiniyield.sequences.benchmarks.kt.every.EveryKt;
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
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class EveryStringBenchmarkTest {


    private EveryStringBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new EveryStringBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.init();
    }

    @Test
    public void testSameResultsEvery() {
        assertTrue(instance.every(instance.lstA.stream(), instance.lstB.stream(), String::equals));
        assertTrue(instance.every(StreamEx.of(instance.lstA), StreamEx.of(instance.lstB), String::equals));
        assertTrue(instance.every(Query.fromList(instance.lstA), Query.fromList(instance.lstB), String::equals));
        assertTrue(instance.every(Seq.seq(instance.lstA), Seq.seq(instance.lstB), String::equals));
        assertTrue(instance.every(Stream.ofAll(instance.lstA), Stream.ofAll(instance.lstB), String::equals));
        assertTrue(instance.everyProtonpack(instance.lstA.stream(), instance.lstB.stream(), String::equals));
        assertTrue(instance.everyGuava(instance.lstA.stream(), instance.lstB.stream(), String::equals));
        assertTrue(instance.everyZipline(instance.lstA.stream(), instance.lstB.stream(), String::equals));
        assertTrue(EveryKt.every(asSequence(instance.lstA), asSequence(instance.lstB), String::equals));
        assertTrue(instance.every(asSequence(instance.lstA), asSequence(instance.lstB), String::equals));
        assertTrue(instance.every(Lists.immutable.ofAll(instance.lstA).asLazy(), Lists.immutable.ofAll(instance.lstB).asLazy(), String::equals));
    }

    @Test
    public void testEverySuccess() {
        String[] input = {"1", "2", "3"};
        assertTrue(instance.every(Arrays.stream(input), Arrays.stream(input), String::equals));
        assertTrue(instance.every(StreamEx.of(input), StreamEx.of(input), String::equals));
        assertTrue(instance.every(Query.of(input), Query.of(input), String::equals));
        assertTrue(instance.every(Seq.of(input), Seq.of(input), String::equals));
        assertTrue(instance.everyProtonpack(Arrays.stream(input), Arrays.stream(input), String::equals));
        assertTrue(instance.every(Stream.of(input), Stream.of(input), String::equals));
        assertTrue(instance.everyGuava(Arrays.stream(input), Arrays.stream(input), String::equals));
        assertTrue(instance.everyZipline(Arrays.stream(input), Arrays.stream(input), String::equals));
        assertTrue(EveryKt.every(ArraysKt.asSequence(input), ArraysKt.asSequence(input), String::equals));
        assertTrue(instance.every(ArraysKt.asSequence(input), ArraysKt.asSequence(input), String::equals));
        assertTrue(instance.every(Lists.immutable.of(input).asLazy(), Lists.immutable.of(input).asLazy(), String::equals));
    }


    @Test
    public void testEveryFailure() {
        String[] input1 = {"1", "2", "3"};
        String[] input2 = {"1", "1", "1"};
        assertFalse(instance.every(Arrays.stream(input1), Arrays.stream(input2), String::equals));
        assertFalse(instance.every(StreamEx.of(input1), StreamEx.of(input2), String::equals));
        assertFalse(instance.every(Query.of(input1), Query.of(input2), String::equals));
        assertFalse(instance.every(Seq.of(input1), Seq.of(input2), String::equals));
        assertFalse(instance.everyProtonpack(Arrays.stream(input1), Arrays.stream(input2), String::equals));
        assertFalse(instance.every(Stream.of(input1), Stream.of(input2), String::equals));
        assertFalse(instance.everyGuava(Arrays.stream(input1), Arrays.stream(input2), String::equals));
        assertFalse(instance.everyZipline(Arrays.stream(input1), Arrays.stream(input2), String::equals));
        assertFalse(EveryKt.every(ArraysKt.asSequence(input1), ArraysKt.asSequence(input2), String::equals));
        assertFalse(instance.every(ArraysKt.asSequence(input1), ArraysKt.asSequence(input2), String::equals));
        assertFalse(instance.every(Lists.immutable.of(input1).asLazy(), Lists.immutable.of(input2).asLazy(), String::equals));
    }

}

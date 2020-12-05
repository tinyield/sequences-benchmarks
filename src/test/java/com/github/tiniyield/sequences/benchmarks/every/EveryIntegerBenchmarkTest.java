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
import com.tinyield.Sek;

import java.util.Arrays;

import static kotlin.collections.CollectionsKt.asSequence;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class EveryIntegerBenchmarkTest {

    private EveryIntegerBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new EveryIntegerBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.init();
    }

    @Test
    public void testSameResultsEvery() {
        assertTrue(instance.every(instance.lstA.stream(), instance.lstB.stream(), Integer::equals));
        assertTrue(instance.every(StreamEx.of(instance.lstA), StreamEx.of(instance.lstB), Integer::equals));
        assertTrue(instance.every(Query.fromList(instance.lstA), Query.fromList(instance.lstB), Integer::equals));
        assertTrue(instance.every(Seq.seq(instance.lstA), Seq.seq(instance.lstB), Integer::equals));
        assertTrue(instance.every(Stream.ofAll(instance.lstA), Stream.ofAll(instance.lstB), Integer::equals));
        assertTrue(instance.everyProtonpack(instance.lstA.stream(), instance.lstB.stream(), Integer::equals));
        assertTrue(instance.everyGuava(instance.lstA.stream(), instance.lstB.stream(), Integer::equals));
        assertTrue(instance.everyZipline(instance.lstA.stream(), instance.lstB.stream(), Integer::equals));
        assertTrue(EveryKt.every(asSequence(instance.lstA), asSequence(instance.lstB), Integer::equals));
        assertTrue(instance.every(asSequence(instance.lstA), asSequence(instance.lstB), Integer::equals));
        assertTrue(instance.every(Lists.immutable.ofAll(instance.lstA).asLazy(), Lists.immutable.ofAll(instance.lstB).asLazy(), Integer::equals));
        assertTrue(instance.every(Sek.of(instance.lstA), Sek.of(instance.lstB), Integer::equals));
    }

    @Test
    public void testEverySuccess() {
        Integer[] input = {1, 2, 3};
        assertTrue(instance.every(Arrays.stream(input), Arrays.stream(input), Integer::equals));
        assertTrue(instance.every(StreamEx.of(input), StreamEx.of(input), Integer::equals));
        assertTrue(instance.every(Query.of(input), Query.of(input), Integer::equals));
        assertTrue(instance.every(Seq.of(input), Seq.of(input), Integer::equals));
        assertTrue(instance.everyProtonpack(Arrays.stream(input), Arrays.stream(input), Integer::equals));
        assertTrue(instance.every(Stream.of(input), Stream.of(input), Integer::equals));
        assertTrue(instance.everyGuava(Arrays.stream(input), Arrays.stream(input), Integer::equals));
        assertTrue(instance.everyZipline(Arrays.stream(input), Arrays.stream(input), Integer::equals));
        assertTrue(EveryKt.every(ArraysKt.asSequence(input), ArraysKt.asSequence(input), Integer::equals));
        assertTrue(instance.every(ArraysKt.asSequence(input), ArraysKt.asSequence(input), Integer::equals));
        assertTrue(instance.every(Lists.immutable.of(input).asLazy(), Lists.immutable.of(input).asLazy(), Integer::equals));
        assertTrue(instance.every(Sek.of(input), Sek.of(input), Integer::equals));
    }


    @Test
    public void testEveryFailure() {
        Integer[] input1 = {1, 2, 3};
        Integer[] input2 = {1, 1, 1};
        assertFalse(instance.every(Arrays.stream(input1), Arrays.stream(input2), Integer::equals));
        assertFalse(instance.every(StreamEx.of(input1), StreamEx.of(input2), Integer::equals));
        assertFalse(instance.every(Query.of(input1), Query.of(input2), Integer::equals));
        assertFalse(instance.every(Seq.of(input1), Seq.of(input2), Integer::equals));
        assertFalse(instance.everyProtonpack(Arrays.stream(input1), Arrays.stream(input2), Integer::equals));
        assertFalse(instance.every(Stream.of(input1), Stream.of(input2), Integer::equals));
        assertFalse(instance.everyGuava(Arrays.stream(input1), Arrays.stream(input2), Integer::equals));
        assertFalse(instance.everyZipline(Arrays.stream(input1), Arrays.stream(input2), Integer::equals));
        assertFalse(EveryKt.every(ArraysKt.asSequence(input1), ArraysKt.asSequence(input2), Integer::equals));
        assertFalse(instance.every(ArraysKt.asSequence(input1), ArraysKt.asSequence(input2), Integer::equals));
        assertFalse(instance.every(Lists.immutable.of(input1).asLazy(), Lists.immutable.of(input2).asLazy(), Integer::equals));
        assertFalse(instance.every(Sek.of(input1), Sek.of(input2), Integer::equals));
    }

}

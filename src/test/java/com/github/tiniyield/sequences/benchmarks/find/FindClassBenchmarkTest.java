package com.github.tiniyield.sequences.benchmarks.find;

import com.github.tiniyield.sequences.benchmarks.kt.find.FindKt;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import io.vavr.collection.Stream;
import kotlin.collections.ArraysKt;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class FindClassBenchmarkTest {


    private FindClassBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new FindClassBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.init();
        instance.update();
    }

    @Test
    public void testSameResultsFind() {
        Value expected = new Value(1);
        assertEquals(instance.findEqualInStream(), expected);
        assertEquals(instance.findEqualInStreamEx(), expected);
        assertEquals(instance.findEqualInQuery(), expected);
        assertEquals(instance.findEqualInJool(), expected);
        assertEquals(instance.findEqualInProtonpack(), expected);
        assertEquals(instance.findEqualInVavr(), expected);
        assertEquals(instance.findEqualInGuava(), expected);
        assertEquals(instance.findEqualInZipline(), expected);
        assertEquals(instance.findEqualInKotlin(), expected);
        assertEquals(instance.findEqualInJKotlin(), expected);
    }

    @Test
    public void testFindSuccess() {
        Value expected = new Value(1);
        Value[] input = {new Value(1), new Value(2), new Value(3)};
        assertEquals(instance.find(Arrays.stream(input), Arrays.stream(input), Value::equals), expected);
        assertEquals(instance.find(StreamEx.of(input), StreamEx.of(input), Value::equals), expected);
        assertEquals(instance.find(Query.of(input), Query.of(input), Value::equals), expected);
        assertEquals(instance.find(Seq.of(input), Seq.of(input), Value::equals), expected);
        assertEquals(instance.findInProtonpack(Arrays.stream(input), Arrays.stream(input), Value::equals), expected);
        assertEquals(instance.find(Stream.of(input), Stream.of(input), Value::equals), expected);
        assertEquals(instance.findInGuava(Arrays.stream(input), Arrays.stream(input), Value::equals), expected);
        assertEquals(instance.findInZipline(Arrays.stream(input), Arrays.stream(input), Value::equals), expected);
        assertEquals(FindKt.find(ArraysKt.asSequence(input), ArraysKt.asSequence(input), Value::equals), expected);
        assertEquals(instance.find(ArraysKt.asSequence(input), ArraysKt.asSequence(input), Value::equals), expected);
    }


    @Test
    public void testFindFailure() {
        Value[] input1 = {new Value(1), new Value(2), new Value(3)};
        Value[] input2 = {new Value(2), new Value(3), new Value(1)};
        assertNull(instance.find(Arrays.stream(input1), Arrays.stream(input2), Value::equals));
        assertNull(instance.find(StreamEx.of(input1), StreamEx.of(input2), Value::equals));
        assertNull(instance.find(Query.of(input1), Query.of(input2), Value::equals));
        assertNull(instance.find(Seq.of(input1), Seq.of(input2), Value::equals));
        assertNull(instance.findInProtonpack(Arrays.stream(input1), Arrays.stream(input2), Value::equals));
        assertNull(instance.find(Stream.of(input1), Stream.of(input2), Value::equals));
        assertNull(instance.findInGuava(Arrays.stream(input1), Arrays.stream(input2), Value::equals));
        assertNull(instance.findInZipline(Arrays.stream(input1), Arrays.stream(input2), Value::equals));
        assertNull(FindKt.find(ArraysKt.asSequence(input1), ArraysKt.asSequence(input2), Value::equals));
        assertNull(instance.find(ArraysKt.asSequence(input1), ArraysKt.asSequence(input2), Value::equals));
    }
}

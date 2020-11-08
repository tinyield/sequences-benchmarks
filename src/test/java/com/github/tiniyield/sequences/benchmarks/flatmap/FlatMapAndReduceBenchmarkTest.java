package com.github.tiniyield.sequences.benchmarks.flatmap;

import com.github.tiniyield.sequences.benchmarks.kt.flatmap.FlatmapAndReduceKt;
import kotlin.collections.CollectionsKt;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class FlatMapAndReduceBenchmarkTest {
    private FlatMapAndReduceBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new FlatMapAndReduceBenchmark();
        instance.COLLECTION_SIZE = 3;
        instance.setup();
    }


    @Test
    public void testSameOutput() {
        Integer expected = 3;
        assertEquals(expected, instance.flatMapAndReduceStream(instance.getNestedSequence(List::stream, List::stream)));
        assertEquals(expected, instance.flatMapAndReduceStreamEx(instance.getNestedSequence(StreamEx::of, StreamEx::of)));
//        assertEquals(expected, instance.flatMapAndReduceQuery(instance.getNestedSequence(Query::fromList, Query::fromList)));
        assertEquals(expected, instance.flatMapAndReduceJool(instance.getNestedSequence(Seq::seq, Seq::seq)));
        assertEquals(expected, instance.flatMapAndReduceVavr(instance.getNestedSequence(io.vavr.collection.Stream::ofAll, io.vavr.collection.Stream::ofAll)));
        assertEquals(expected.intValue(), FlatmapAndReduceKt.flatMapAndReduce(instance.getNestedSequence(CollectionsKt::asSequence, CollectionsKt::asSequence)));
        assertEquals(expected, instance.flatMapAndReduceJKotlin(instance.getNestedSequence(CollectionsKt::asSequence, CollectionsKt::asSequence)));
    }

}

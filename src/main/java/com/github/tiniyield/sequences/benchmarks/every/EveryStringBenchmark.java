package com.github.tiniyield.sequences.benchmarks.every;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class EveryStringBenchmark extends EveryBenchmark<String, String> {
    /**
     * lstA and lstB are two Lists with the same String  objects.
     */
    private List<String> lstA;
    private List<String> lstB;

    @Override
    protected List<String> getListA() {
        return lstA;
    }

    @Override
    protected List<String> getListB() {
        return lstB;
    }

    @Override
    protected BiPredicate<String, String> getPredicate() {
        return String::equals;
    }

    @Setup
    public void init() {
        lstB = new ArrayList<>(COLLECTION_SIZE);
        lstA = IntStream
                .rangeClosed(1, COLLECTION_SIZE)
                .mapToObj(String::valueOf)
                .collect(Collectors.toList());
        lstB.addAll(lstA);
    }

}

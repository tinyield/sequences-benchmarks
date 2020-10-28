package com.github.tiniyield.sequences.benchmarks.find;

import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.IntegerDataProvider;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindStringBenchmark extends FindBenchmark<String> {
    private List<String> lstA;
    private List<String> lstB;


    protected List<String> getListA() {
        return lstA;
    }


    protected List<String> getListB() {
        return lstB;
    }


    protected BiPredicate<String, String> getPredicate() {
        return String::equals;
    }

    @Setup
    public void init() {
        lstA = new IntegerDataProvider(COLLECTION_SIZE)
                .asStream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        lstB = new IntegerDataProvider(COLLECTION_SIZE)
                .asStream()
                .map(v -> -1)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    protected void update() {
        lstB.set((index - 1) % COLLECTION_SIZE, String.valueOf(-1));
        lstB.set(index % COLLECTION_SIZE, String.valueOf(index % COLLECTION_SIZE));
    }

}

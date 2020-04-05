package com.github.tiniyield.sequences.benchmarks.find;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.IntegerDataProvider;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindStringBenchmark extends FindBenchmark<String> {
    private List<List<String>> matrixA;
    private List<List<String>> matrixB;


    protected List<String> getListA() {
        return matrixA.get(index % COLLECTION_SIZE);
    }


    protected List<String> getListB() {
        return matrixB.get(index % COLLECTION_SIZE);
    }


    protected BiPredicate<String, String> getPredicate() {
        return String::equals;
    }


    @Setup
    public void init() {
        matrixA = new ArrayList<>(COLLECTION_SIZE);
        matrixB = new ArrayList<>(COLLECTION_SIZE);
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            matrixA.add(
                    new IntegerDataProvider(COLLECTION_SIZE)
                            .asStream()
                            .map(String::valueOf)
                            .collect(Collectors.toList())
            );
            int iFinal = i;
            matrixB.add(
                    new IntegerDataProvider(COLLECTION_SIZE)
                            .asStream()
                            .map(v -> iFinal)
                            .map(String::valueOf)
                            .collect(Collectors.toList())
            );
        }
    }

}

package com.github.tiniyield.sequences.benchmarks.zip;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.assertZipPrimeWithValueValidity;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getNumbersDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getValueDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.initNumbersDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.initValueDataProvider;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.javatuples.Pair;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.github.tiniyield.sequences.benchmarks.IZipBenchmark;
import com.github.tiniyield.sequences.benchmarks.operations.GuavaOperations;
import com.github.tiniyield.sequences.benchmarks.operations.JoolOperations;
import com.github.tiniyield.sequences.benchmarks.operations.ProtonpackOperations;
import com.github.tiniyield.sequences.benchmarks.operations.QueryOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamExOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamOperations;
import com.github.tiniyield.sequences.benchmarks.operations.VavrOperations;
import com.github.tiniyield.sequences.benchmarks.operations.ZiplineOperations;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;

import one.util.streamex.StreamEx;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class VanillaZipBenchmark extends AbstractZipBenchmark<Pair<Integer, Value>> {

    @Param({"10000"})
    protected int COLLECTION_SIZE;

    @Setup
    public void init() {

        initNumbersDataProvider(COLLECTION_SIZE);
        initValueDataProvider(COLLECTION_SIZE);
        assertZipPrimeWithValueValidity(getGuava(),
                                        getJool(),
                                        getQuery(),
                                        getStream(),
                                        getProtonpack(),
                                        getZipline(),
                                        getStreamEx(),
                                        getVavr());
    }

    protected io.vavr.collection.Stream<Pair<Integer, Value>> getVavr() {
        return VavrOperations.zipPrimeWithValue();
    }

    protected StreamEx<Pair<Integer, Value>> getStreamEx() {
        return StreamExOperations.zipPrimeWithValue();
    }

    protected Stream<Pair<Integer, Value>> getZipline() {
        return ZiplineOperations.zipPrimeWithValue();
    }

    protected Stream<Pair<Integer, Value>> getProtonpack() {
        return ProtonpackOperations.zipPrimeWithValue();
    }

    protected Stream<Pair<Integer, Value>> getStream() {
        return StreamOperations.zipPrimeWithValue();
    }

    protected Query<Pair<Integer, Value>> getQuery() {
        return QueryOperations.zipPrimeWithValue();
    }

    protected Stream<Pair<Integer, Value>> getGuava() {
        return guava.zipPrimeWithValue(
                getNumbersDataProvider().asStream(),
                getValueDataProvider().asStream()
        );
    }

    protected Seq<Pair<Integer, Value>> getJool() {
        return jool.zipPrimeWithValue(
                getNumbersDataProvider().asSeq(),
                getValueDataProvider().asSeq()
        );
    }


}

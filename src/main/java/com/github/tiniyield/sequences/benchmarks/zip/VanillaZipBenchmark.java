package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
import org.javatuples.Pair;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.assertZipPrimeWithValueValidity;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getNumbersDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.getValueDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.initNumbersDataProvider;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkUtils.initValueDataProvider;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class VanillaZipBenchmark extends AbstractZipBenchmark<Pair<Integer, Value>> {

    @Param({"10000"})
    protected int COLLECTION_SIZE;

    @Override
    @Setup
    public void init() {

        initNumbersDataProvider(COLLECTION_SIZE);
        initValueDataProvider(COLLECTION_SIZE);
        assertZipPrimeWithValueValidity(
                getGuava(),
                getJool(),
                getQuery(),
                getStream(),
                getProtonpack(),
                getZipline(),
                getStreamEx(),
                getVavr(),
                getKotlin(),
                getJKotlin()
        );
    }

    @Override
    protected io.vavr.collection.Stream<Pair<Integer, Value>> getVavr() {
        return vavr.zipPrimeWithValue(getNumbersDataProvider().asVavrStream(),
                getValueDataProvider().asVavrStream());
    }

    @Override
    protected StreamEx<Pair<Integer, Value>> getStreamEx() {
        return streamEx.zipPrimeWithValue(getNumbersDataProvider().asStreamEx(),
                getValueDataProvider().asStreamEx());
    }

    @Override
    protected Stream<Pair<Integer, Value>> getZipline() {
        return zipline.zipPrimeWithValue(getNumbersDataProvider().asStream(),
                getValueDataProvider().asStream());
    }

    @Override
    protected Stream<Pair<Integer, Value>> getProtonpack() {
        return protonpack.zipPrimeWithValue(getNumbersDataProvider().asStream(),
                getValueDataProvider().asStream());
    }

    @Override
    protected Stream<Pair<Integer, Value>> getStream() {
        return stream.zipPrimeWithValue(getNumbersDataProvider().asStream(),
                getValueDataProvider().asStream());
    }

    @Override
    protected Query<Pair<Integer, Value>> getQuery() {
        return query.zipPrimeWithValue(getNumbersDataProvider().asQuery(), getValueDataProvider().asQuery());
    }

    @Override
    protected Stream<Pair<Integer, Value>> getGuava() {
        return guava.zipPrimeWithValue(
                getNumbersDataProvider().asStream(),
                getValueDataProvider().asStream()
        );
    }

    @Override
    protected Seq<Pair<Integer, Value>> getJool() {
        return jool.zipPrimeWithValue(
                getNumbersDataProvider().asSeq(),
                getValueDataProvider().asSeq()
        );
    }

    @Override
    protected Sequence<Pair<Integer, Value>> getKotlin() {
        return kotlin.zipPrimeWithValue(
                getNumbersDataProvider().asSequence(),
                getValueDataProvider().asSequence()
        );
    }

    @Override
    protected Sequence<Pair<Integer, Value>> getJKotlin() {
        return jkotlin.zipPrimeWithValue(
                getNumbersDataProvider().asSequence(),
                getValueDataProvider().asSequence()
        );
    }


}

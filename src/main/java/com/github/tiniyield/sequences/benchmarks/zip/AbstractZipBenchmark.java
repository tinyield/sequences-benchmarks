package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.AbstractZipOperationsBenchmark;
import kotlin.Unit;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public abstract class AbstractZipBenchmark<T> extends AbstractZipOperationsBenchmark {

    protected abstract io.vavr.collection.Stream<T> getVavr();

    protected abstract StreamEx<T> getStreamEx();

    protected abstract Stream<T> getZipline();

    protected abstract Stream<T> getProtonpack();

    protected abstract Stream<T> getStream();

    protected abstract Query<T> getQuery();

    protected abstract Stream<T> getGuava();

    protected abstract Seq<T> getJool();

    protected abstract Sequence<T> getKotlin();

    protected abstract Sequence<T> getJKotlin();

    @Override
    protected abstract void init();

    @Setup
    public void setup() {
        super.init();
        this.init();
    }

    @Benchmark
    public final void stream(Blackhole bh) { // With Auxiliary Function
        getStream().forEach(bh::consume);
    }

    @Benchmark
    public final void streamEx(Blackhole bh) {
        getStreamEx().forEach(bh::consume);
    }

    @Benchmark
    public final void jayield(Blackhole bh) {
        getQuery().traverse(bh::consume);
    }

    @Benchmark
    public final void jool(Blackhole bh) {
        getJool().forEach(bh::consume);
    }

    // Other Sequences based benchmarks

    @Benchmark
    public final void vavr(Blackhole bh) {
        getVavr().forEach(bh::consume);
    }

    @Benchmark
    public final void protonpack(Blackhole bh) {
        getProtonpack().forEach(bh::consume);
    }

    @Benchmark
    public final void guava(Blackhole bh) {
        getGuava().forEach(bh::consume);
    }

    @Benchmark
    public final void zipline(Blackhole bh) {
        getZipline().forEach(bh::consume);
    }

    @Benchmark
    public final void kotlin(Blackhole bh) {
        SequencesKt.forEach(getKotlin(), elem -> {
            bh.consume(elem);
            return Unit.INSTANCE;
        });
    }

    @Benchmark
    public final void jkotlin(Blackhole bh) {
        SequencesKt.forEach(getJKotlin(), elem -> {
            bh.consume(elem);
            return Unit.INSTANCE;
        });
    }
}

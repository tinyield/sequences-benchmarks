package com.github.tiniyield.sequences.benchmarks.zip;

import com.codepoetics.protonpack.StreamUtils;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ZipPrimesWithValuesKt;
import com.github.tiniyield.sequences.benchmarks.operations.CustomStreamOperations;
import com.github.tiniyield.sequences.benchmarks.operations.model.wrapper.Value;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import one.util.streamex.StreamEx;
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

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.google.common.collect.Streams.zip;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class VanillaZipBenchmark {

    @Param({"10000"})
    public int COLLECTION_SIZE;

    private Integer[] numbers;
    private Value[] values;

    public static boolean isPrime(Integer value) {
        if (value <= 1) {
            return false;
        }
        if (value <= 3) {
            return true;
        }
        if (value % 2 == 0) {
            return false;
        }
        int i = 3;
        while (i * i <= value) {
            if (value % i == 0) {
                return false;
            }
            i += 2;
        }
        return true;
    }

    public Integer[] getNumbers() {
        Integer[] numbers = new Integer[COLLECTION_SIZE];
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            numbers[i] = i;
        }
        return numbers;
    }

    @Setup
    public void init() {
        numbers = getNumbers();
        values = Arrays.stream(numbers)
                .map(Value::new)
                .toArray(Value[]::new);
    }


    public io.vavr.collection.Stream<Pair<Integer, Value>> zipPrimeWithValue(io.vavr.collection.Stream<Integer> numbers, io.vavr.collection.Stream<Value> values) {
        return numbers.filter(VanillaZipBenchmark::isPrime).zipWith(values, Pair::with);
    }

    public Stream<Pair<Integer, Value>> zipPrimeWithValueGuava(Stream<Integer> numbers, Stream<Value> values) {
        return zip(numbers.filter(VanillaZipBenchmark::isPrime), values, Pair::with);
    }

    public Sequence<Pair<Integer, Value>> zipPrimeWithValue(Sequence<Integer> numbers, Sequence<Value> values) {
        return SequencesKt.zip(
                SequencesKt.filter(numbers, VanillaZipBenchmark::isPrime),
                values,
                Pair::with
        );
    }

    public Stream<Pair<Integer, Value>> zipPrimeWithValueZipline(Stream<Integer> numbers, Stream<Value> values) {
        Iterator<Value> iter = values.iterator();
        return numbers.filter(VanillaZipBenchmark::isPrime).map(v -> Pair.with(v, iter.next()));
    }

    public Stream<Pair<Integer, Value>> zipPrimeWithValue(Stream<Integer> numbers, Stream<Value> values) {
        return CustomStreamOperations.zip(numbers.filter(VanillaZipBenchmark::isPrime), values, Pair::with);
    }

    public StreamEx<Pair<Integer, Value>> zipPrimeWithValue(StreamEx<Integer> numbers, StreamEx<Value> values) {
        return numbers.filter(VanillaZipBenchmark::isPrime).zipWith(values, Pair::with);
    }

    public Seq<Pair<Integer, Value>> zipPrimeWithValue(Seq<Integer> numbers, Seq<Value> values) {
        return numbers.filter(VanillaZipBenchmark::isPrime).zip(values, Pair::with);
    }

    public Query<Pair<Integer, Value>> zipPrimeWithValue(Query<Integer> numbers, Query<Value> values) {
        return numbers.filter(VanillaZipBenchmark::isPrime).zip(values, Pair::with);
    }

    public Stream<Pair<Integer, Value>> zipPrimeWithValueProtonpack(Stream<Integer> numbers, Stream<Value> values) {
        return StreamUtils.zip(numbers.filter(VanillaZipBenchmark::isPrime), values, Pair::with);
    }

    protected io.vavr.collection.Stream<Pair<Integer, Value>> getVavr() {
        return zipPrimeWithValue(io.vavr.collection.Stream.of(numbers),
                io.vavr.collection.Stream.of(values));
    }

    protected StreamEx<Pair<Integer, Value>> getStreamEx() {
        return zipPrimeWithValue(StreamEx.of(numbers),
                StreamEx.of(values));
    }

    protected Stream<Pair<Integer, Value>> getZipline() {
        return zipPrimeWithValueZipline(Arrays.stream(numbers),
                Arrays.stream(values));
    }

    protected Stream<Pair<Integer, Value>> getProtonpack() {
        return zipPrimeWithValueProtonpack(Arrays.stream(numbers),
                Arrays.stream(values));
    }

    protected Stream<Pair<Integer, Value>> getStream() {
        return zipPrimeWithValue(Arrays.stream(numbers),
                Arrays.stream(values));
    }

    protected Query<Pair<Integer, Value>> getQuery() {
        return zipPrimeWithValue(Query.of(numbers), Query.of(values));
    }

    protected Stream<Pair<Integer, Value>> getGuava() {
        return zipPrimeWithValueGuava(
                Arrays.stream(numbers),
                Arrays.stream(values)
        );
    }

    protected Seq<Pair<Integer, Value>> getJool() {
        return zipPrimeWithValue(
                Seq.of(numbers),
                Seq.of(values)
        );
    }

    protected Sequence<Pair<Integer, Value>> getKotlin() {
        return ZipPrimesWithValuesKt.zipPrimeWithValue(
                ArraysKt.asSequence(numbers),
                ArraysKt.asSequence(values)
        );
    }

    protected Sequence<Pair<Integer, Value>> getJKotlin() {
        return zipPrimeWithValue(
                ArraysKt.asSequence(numbers),
                ArraysKt.asSequence(values)
        );
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

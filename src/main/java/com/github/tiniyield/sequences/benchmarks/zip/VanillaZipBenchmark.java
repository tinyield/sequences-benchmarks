package com.github.tiniyield.sequences.benchmarks.zip;

import com.codepoetics.protonpack.StreamUtils;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ZipPrimesWithValuesKt;
import com.github.tiniyield.sequences.benchmarks.common.model.wrapper.Value;
import kotlin.Unit;
import kotlin.sequences.Sequence;
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
import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.sequences.SequencesKt.filter;
import static kotlin.sequences.SequencesKt.forEach;
import static kotlin.sequences.SequencesKt.zip;

/**
 * VanillaZipBenchmark
 * Benchmarks zipping a sequence of prime Integers with instances of the class Value.
 *
 * Pipeline:
 * Sequence.of(new Integer[]{1, 2, ..., n})
 * .filter(isPrime)
 * .zip(Sequence.of(new Value[]{new Value(1), new Value(2), ..., new Value(n)}), Pair::with)
 * .forEach(bh::consume)
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class VanillaZipBenchmark {

    /**
     * The size of the Sequence for this benchmark
     */
    @Param({"10000"})
    public int COLLECTION_SIZE;

    /**
     * The numbers data source used to benchmark
     * This data is instantiated using the getNumbers method.
     */
    public Integer[] numbers;
    /**
     * The values data source used to benchmark
     * This data is instantiated using the getNumbers method and mapping the numbers to Values.
     */
    public Value[] values;

    /**
     * Creates an array of Integers from 0 to COLLECTION_SIZE
     * @return an array of Integers
     */
    public Integer[] getNumbers() {
        Integer[] ns = new Integer[COLLECTION_SIZE];
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            ns[i] = i;
        }
        return ns;
    }

    /**
     * Sets up the data sources to be used in this benchmark
     */
    @Setup
    public void init() {
        numbers = getNumbers();
        values = Arrays.stream(numbers)
                .map(Value::new)
                .toArray(Value[]::new);
    }

    /**
     * Runs this benchmark using {@link Stream}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void stream(Blackhole bh) { // With Auxiliary Function
        zipPrimeWithValue(Arrays.stream(numbers),Arrays.stream(values)).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link StreamEx}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void streamEx(Blackhole bh) {
        zipPrimeWithValue(StreamEx.of(numbers),StreamEx.of(values)).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link Query}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void jayield(Blackhole bh) {
        zipPrimeWithValue(Query.of(numbers), Query.of(values)).traverse(bh::consume);
    }

    /**
     * Runs this benchmark using {@link Seq}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void jool(Blackhole bh) {
        zipPrimeWithValue(Seq.of(numbers),Seq.of(values)).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link io.vavr.collection.Stream}s in it's pipeline
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void vavr(Blackhole bh) {
        zipPrimeWithValue(io.vavr.collection.Stream.of(numbers), io.vavr.collection.Stream.of(values)).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Protonpack in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void protonpack(Blackhole bh) {
        zipPrimeWithValueProtonpack(Arrays.stream(numbers),Arrays.stream(values)).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Guava in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void guava(Blackhole bh) {
        zipPrimeWithValueGuava(Arrays.stream(numbers),Arrays.stream(values)).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s and the
     * zipline approach in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void zipline(Blackhole bh) {
        zipPrimeWithValueZipline(Arrays.stream(numbers), Arrays.stream(values)).forEach(bh::consume);
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void kotlin(Blackhole bh) {
        forEach(
                ZipPrimesWithValuesKt.zipPrimeWithValue(asSequence(numbers), asSequence(values)),
                elem -> {
                    bh.consume(elem);
                    return Unit.INSTANCE;
                }
        );
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Java in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations
     */
    @Benchmark
    public final void jkotlin(Blackhole bh) {
        forEach(
                zipPrimeWithValue(asSequence(numbers), asSequence(values)),
                elem -> {
                    bh.consume(elem);
                    return Unit.INSTANCE;
                }
        );
    }

    /**
     * Filters the prime numbers {@link io.vavr.collection.Stream} from the {@param numbers} and then zips the resulting
     * {@link io.vavr.collection.Stream} sequence with the {@param values}
     * @param numbers the numbers to filter
     * @param values the values to pair with
     * @return a {@link io.vavr.collection.Stream} sequence of Pairs between prime numbers and values
     */
    public io.vavr.collection.Stream<Pair<Integer, Value>> zipPrimeWithValue(io.vavr.collection.Stream<Integer> numbers, io.vavr.collection.Stream<Value> values) {
        return numbers.filter(IsPrime::isPrime).zipWith(values, Pair::with);
    }

    /**
     * Filters the prime numbers {@link Stream} from the {@param numbers} and then zips the resulting {@link Stream}
     * sequence with the {@param values}, using Guava
     * @param numbers the numbers to filter
     * @param values the values to pair with
     * @return a {@link Stream} sequence of Pairs between prime numbers and values
     */
    public Stream<Pair<Integer, Value>> zipPrimeWithValueGuava(Stream<Integer> numbers, Stream<Value> values) {
        return zip(numbers.filter(IsPrime::isPrime), values, Pair::with);
    }

    /**
     * Filters the kotlin prime numbers {@link Sequence} in Java from the {@param numbers} and then zips the resulting
     * {@link Sequence} in Java with the {@param values}
     * @param numbers the numbers to filter
     * @param values the values to pair with
     * @return a Kotlin {@link Sequence} in Java of Pairs between prime numbers and values
     */
    public Sequence<Pair<Integer, Value>> zipPrimeWithValue(Sequence<Integer> numbers, Sequence<Value> values) {
        return zip(filter(numbers, IsPrime::isPrime), values, Pair::with);
    }

    /**
     * Filters the prime numbers {@link Stream} from the {@param numbers} and then zips the resulting {@link Stream}
     * sequence with the {@param values}, using the zipline approach
     * @param numbers the numbers to filter
     * @param values the values to pair with
     * @return a {@link Stream} sequence of Pairs between prime numbers and values
     */
    public Stream<Pair<Integer, Value>> zipPrimeWithValueZipline(Stream<Integer> numbers, Stream<Value> values) {
        Iterator<Value> iter = values.iterator();
        return numbers.filter(IsPrime::isPrime).map(v -> Pair.with(v, iter.next()));
    }

    /**
     * Filters the prime numbers {@link Stream} from the {@param numbers} and then zips the resulting {@link Stream}
     * sequence with the {@param values}
     * @param numbers the numbers to filter
     * @param values the values to pair with
     * @return a {@link Stream} sequence of Pairs between prime numbers and values
     */
    public Stream<Pair<Integer, Value>> zipPrimeWithValue(Stream<Integer> numbers, Stream<Value> values) {
        return StreamZipOperation.zip(numbers.filter(IsPrime::isPrime), values, Pair::with);
    }

    /**
     * Filters the prime numbers {@link StreamEx} from the {@param numbers} and then zips the resulting {@link StreamEx}
     * sequence with the {@param values}
     * @param numbers the numbers to filter
     * @param values the values to pair with
     * @return a {@link StreamEx} sequence of Pairs between prime numbers and values
     */
    public StreamEx<Pair<Integer, Value>> zipPrimeWithValue(StreamEx<Integer> numbers, StreamEx<Value> values) {
        return numbers.filter(IsPrime::isPrime).zipWith(values, Pair::with);
    }

    /**
     * Filters the prime numbers {@link Seq} from the {@param numbers} and then zips the resulting {@link Seq}
     * sequence with the {@param values}
     * @param numbers the numbers to filter
     * @param values the values to pair with
     * @return a {@link Seq} sequence of Pairs between prime numbers and values
     */
    public Seq<Pair<Integer, Value>> zipPrimeWithValue(Seq<Integer> numbers, Seq<Value> values) {
        return numbers.filter(IsPrime::isPrime).zip(values, Pair::with);
    }

    /**
     * Filters the prime numbers {@link Query} from the {@param numbers} and then zips the resulting {@link Query}
     * sequence with the {@param values}
     * @param numbers the numbers to filter
     * @param values the values to pair with
     * @return a {@link Query} sequence of Pairs between prime numbers and values
     */
    public Query<Pair<Integer, Value>> zipPrimeWithValue(Query<Integer> numbers, Query<Value> values) {
        return numbers.filter(IsPrime::isPrime).zip(values, Pair::with);
    }

    /**
     * Filters the prime numbers {@link Stream} from the {@param numbers} and then zips the resulting {@link Stream}
     * sequence with the {@param values}, using Protonpack
     * @param numbers the numbers to filter
     * @param values the values to pair with
     * @return a {@link Stream} sequence of Pairs between prime numbers and values
     */
    public Stream<Pair<Integer, Value>> zipPrimeWithValueProtonpack(Stream<Integer> numbers, Stream<Value> values) {
        return StreamUtils.zip(numbers.filter(IsPrime::isPrime), values, Pair::with);
    }

}

package com.github.tiniyield.sequences.benchmarks.zip;

import com.codepoetics.protonpack.StreamUtils;
import com.github.tiniyield.sequences.benchmarks.common.model.wrapper.Value;
import com.github.tiniyield.sequences.benchmarks.kt.zip.ZipPrimesWithValuesKt;
import com.google.common.collect.Streams;
import com.tinyield.Sek;
import kotlin.Unit;
import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.impl.factory.Lists;
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

import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.sequences.SequencesKt.filter;
import static kotlin.sequences.SequencesKt.forEach;
import static kotlin.sequences.SequencesKt.zip;

/**
 * VanillaZipBenchmark
 * Benchmarks zipping a sequence of prime Integers with instances of the class Value.
 * <p>
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
     *
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
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void stream(Blackhole bh) { // With Auxiliary Function
        streamPipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link StreamEx}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void streamEx(Blackhole bh) {
        streamExPipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link Query}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void jayield(Blackhole bh) {
        jayieldPipeline().traverse(bh::consume);
    }

    /**
     * Runs this benchmark using {@link Seq}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void jool(Blackhole bh) {
        joolPipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link io.vavr.collection.Stream}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void vavr(Blackhole bh) {
        vavrPipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Protonpack in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void protonpack(Blackhole bh) {
        protonpackPipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s in conjunction
     * with Guava in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void guava(Blackhole bh) {
        guavaPipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link java.util.stream.Stream}s and the
     * zipline approach in it's pipeline.
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void zipline(Blackhole bh) {
        ziplinePipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Kotlin in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void kotlin(Blackhole bh) {
        forEach(
                kotlinPipeline(),
                elem -> {
                    bh.consume(elem);
                    return Unit.INSTANCE;
                }
        );
    }

    /**
     * Runs this benchmark using Kotlin {@link Sequence}s in Java in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void jkotlin(Blackhole bh) {
        forEach(
                jKotlinPipeline(),
                elem -> {
                    bh.consume(elem);
                    return Unit.INSTANCE;
                }
        );
    }

    /**
     * Runs this benchmark using {@link LazyIterable}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void eclipse(Blackhole bh) {
        eclipsePipeline().forEach(bh::consume);
    }

    /**
     * Runs this benchmark using {@link Sek}s in it's pipeline
     *
     * @param bh a Blackhole instance to prevent compiler optimizations and to evaluate traversal performance
     */
    @Benchmark
    public final void sek(Blackhole bh) {
        sekPipeline().forEach(bh::consume);
    }


    /**
     * Filters the kotlin prime numbers {@link Sequence} in Java from the numbers and then zips the resulting
     * {@link Sequence} in Java with the values
     *
     * @return a Kotlin {@link Sequence} in Java of Pairs between prime numbers and values
     */
    public final Sequence<Pair<Integer, Value>> kotlinPipeline() {
        return ZipPrimesWithValuesKt.zipPrimeWithValue(asSequence(numbers), asSequence(values));
    }

    /**
     * Filters the prime numbers {@link io.vavr.collection.Stream} from the numbers and then zips the resulting
     * {@link io.vavr.collection.Stream} sequence with the values
     *
     * @return a {@link io.vavr.collection.Stream} sequence of Pairs between prime numbers and values
     */
    public final io.vavr.collection.Stream<Pair<Integer, Value>> vavrPipeline() {
        return io.vavr.collection.Stream.of(numbers)
                .filter(IsPrime::isPrime)
                .zipWith(io.vavr.collection.Stream.of(values), Pair::with);
    }

    /**
     * Filters the prime numbers {@link Stream} from the numbers and then zips the resulting {@link Stream}
     * sequence with the values, using Guava
     *
     * @return a {@link Stream} sequence of Pairs between prime numbers and values
     */
    public final Stream<Pair<Integer, Value>> guavaPipeline() {
        return Streams.zip(
                Arrays.stream(numbers).filter(IsPrime::isPrime),
                Arrays.stream(values),
                Pair::with
        );
    }

    /**
     * Filters the kotlin prime numbers {@link Sequence} in Java from the numbers and then zips the resulting
     * {@link Sequence} in Java with the values
     *
     * @return a Kotlin {@link Sequence} in Java of Pairs between prime numbers and values
     */
    public final Sequence<Pair<Integer, Value>> jKotlinPipeline() {
        return zip(
                filter(
                        asSequence(numbers),
                        IsPrime::isPrime
                ),
                asSequence(values),
                Pair::with);
    }

    /**
     * Filters the prime numbers {@link Stream} from the numbers and then zips the resulting {@link Stream}
     * sequence with the values, using the zipline approach
     *
     * @return a {@link Stream} sequence of Pairs between prime numbers and values
     */
    public final Stream<Pair<Integer, Value>> ziplinePipeline() {
        Iterator<Value> iter = Arrays.stream(values).iterator();
        return Arrays.stream(numbers)
                .filter(IsPrime::isPrime)
                .map(v -> Pair.with(v, iter.next()));
    }

    /**
     * Filters the prime numbers {@link Stream} from the numbers and then zips the resulting {@link Stream}
     * sequence with the values
     *
     * @return a {@link Stream} sequence of Pairs between prime numbers and values
     */
    public final Stream<Pair<Integer, Value>> streamPipeline() {
        return StreamZipOperation.zip(
                Arrays.stream(numbers).filter(IsPrime::isPrime),
                Arrays.stream(values),
                Pair::with
        );
    }

    /**
     * Filters the prime numbers {@link StreamEx} from the numbers and then zips the resulting {@link StreamEx}
     * sequence with the values
     *
     * @return a {@link StreamEx} sequence of Pairs between prime numbers and values
     */
    public final StreamEx<Pair<Integer, Value>> streamExPipeline() {
        return StreamEx.of(numbers)
                .filter(IsPrime::isPrime)
                .zipWith(StreamEx.of(values), Pair::with);
    }

    /**
     * Filters the prime numbers {@link Seq} from the numbers and then zips the resulting {@link Seq}
     * sequence with the values
     *
     * @return a {@link Seq} sequence of Pairs between prime numbers and values
     */
    public final Seq<Pair<Integer, Value>> joolPipeline() {
        return Seq.of(numbers)
                .filter(IsPrime::isPrime)
                .zip(Seq.of(values), Pair::with);
    }

    /**
     * Filters the prime numbers {@link Query} from the numbers} and then zips the resulting {@link Query}
     * sequence with the @param values
     *
     * @return a {@link Query} sequence of Pairs between prime numbers and values
     */
    public final Query<Pair<Integer, Value>> jayieldPipeline() {
        return Query.of(numbers)
                .filter(IsPrime::isPrime)
                .zip(Query.of(values), Pair::with);
    }

    /**
     * Filters the prime numbers {@link Stream} from the numbers and then zips the resulting {@link Stream}
     * sequence with the values, using Protonpack
     *
     * @return a {@link Stream} sequence of Pairs between prime numbers and values
     */
    public final Stream<Pair<Integer, Value>> protonpackPipeline() {
        return StreamUtils.zip(
                Arrays.stream(numbers).filter(IsPrime::isPrime),
                Arrays.stream(values),
                Pair::with
        );
    }

    /**
     * Filters the prime numbers {@link LazyIterable} from the numbers and then zips the resulting {@link LazyIterable}
     * sequence with the values
     *
     * @return a {@link LazyIterable} sequence of Pairs between prime numbers and values
     */
    public final LazyIterable<Pair<Integer, Value>> eclipsePipeline() {
        return Lists.immutable.with(numbers).asLazy()
                .select(IsPrime::isPrime)
                .zip(Lists.immutable.with(values).asLazy())
                .collect(p -> Pair.with(p.getOne(), p.getTwo()));
    }

    /**
     * Filters the prime numbers {@link Sek} from the numbers and then zips the resulting {@link Sek}
     * sequence with the values
     *
     * @return a {@link Sek} sequence of Pairs between prime numbers and values
     */
    public final Sek<Pair<Integer, Value>> sekPipeline() {
        return Sek.of(numbers)
                .filter(IsPrime::isPrime)
                .zip(Sek.of(values), Pair::with);
    }

}

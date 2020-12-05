package com.github.tiniyield.sequences.benchmarks.collapse;

import com.github.tiniyield.sequences.benchmarks.common.StreamOddLinesOperation;
import com.github.tiniyield.sequences.benchmarks.common.WeatherDataSource;
import com.github.tiniyield.sequences.benchmarks.kt.collapse.CollapseKt;
import com.github.tiniyield.sequences.benchmarks.kt.collapse.YieldCollapseKt;
import com.github.tiniyield.sequences.benchmarks.kt.odd.lines.OddLinesKt;
import com.github.tiniyield.sequences.benchmarks.kt.odd.lines.YieldOddLinesKt;
import io.vavr.control.Option;
import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.factory.Lists;
import org.jayield.Query;
import org.jayield.Traverser;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import com.tinyield.Sek;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.sequences.SequencesKt.count;
import static kotlin.sequences.SequencesKt.drop;
import static kotlin.sequences.SequencesKt.filter;
import static kotlin.sequences.SequencesKt.map;

@BenchmarkMode(Mode.Throughput)
public class QueryNrOfTemperaturesTransitions {

    public static <T> StreamEx<T> oddLines(StreamEx<T> source) {
        return source.headTail((__, t) -> t.headTail((head, tail) -> oddLines(tail).prepend(head)));
    }

    public static <T> StreamEx<T> collapse(StreamEx<T> source) {
        return collapse(source, null);
    }

    public static <T> StreamEx<T> collapse(StreamEx<T> source, T previous) {
        return source.headTail((head, tail) -> {
            if (Objects.equals(head, previous)) {
                return collapse(tail, head);
            } else {
                return collapse(tail, head).prepend(head);
            }
        });
    }

    public static <T> io.vavr.collection.Stream<T> oddLines(io.vavr.collection.Stream<T> source) {
        if (source.isEmpty()) {
            return io.vavr.collection.Stream.empty();
        }
        Option<T> option = source.tail().headOption();
        if (option.isEmpty()) {
            return io.vavr.collection.Stream.empty();
        } else {
            return io.vavr.collection.Stream.cons(option.get(), () -> oddLines(source.tail().tail()));
        }
    }

    public static <T> io.vavr.collection.Stream<T> collapse(io.vavr.collection.Stream<T> source) {
        return collapse(source, null);
    }

    public static <T> io.vavr.collection.Stream<T> collapse(io.vavr.collection.Stream<T> source, T previous) {
        if (source.isEmpty()) {
            return io.vavr.collection.Stream.empty();
        }
        Option<T> option = source.headOption();
        if (option.isEmpty()) {
            return io.vavr.collection.Stream.empty();
        } else {
            T head = option.get();
            if (Objects.equals(head, previous)) {
                return collapse(source.tail(), head);
            } else {
                return collapse(source.tail(), head).prepend(head);
            }
        }
    }

    public static <T> Stream<T> oddLines(Stream<T> source) {
        return StreamSupport.stream(new StreamOddLinesOperation.StreamOddLines<>(source.spliterator()), false);
    }

    public static <T> Stream<T> collapse(Stream<T> source) {
        return StreamSupport.stream(new StreamCollapseOperation.StreamCollapse<>(source.spliterator()), false);
    }

    public static <T> Seq<T> oddLines(Seq<T> source) {
        return Seq.seq(oddLines(source.stream()));
    }

    public static <T> Seq<T> collapse(Seq<T> source) {
        return Seq.seq(collapse(source.stream()));
    }

    public static <U> Traverser<U> oddLines(Query<U> src) {
        return yld -> {
            final boolean[] isOdd = {false};
            src.traverse(item -> {
                if (isOdd[0]) yld.ret(item);
                isOdd[0] = !isOdd[0];
            });
        };
    }

    public static <U> Traverser<U> collapse(Query<U> src) {
        return yld -> {
            final Object[] prev = {null};
            src.traverse(item -> {
                if (prev[0] == null || !prev[0].equals(item))
                    yld.ret((U) (prev[0] = item));
            });
        };
    }

    public static <U> LazyIterable<U> oddLines(LazyIterable<U> src) {
        return Lists.immutable.withAll(() -> {
            Iterator<U> upstream = src.iterator();
            return new Iterator<U>() {
                boolean isOdd = false;

                @Override
                public boolean hasNext() {
                    if (!isOdd) {
                        if (!upstream.hasNext()) {
                            return false;
                        } else {
                            upstream.next();
                        }
                    }
                    isOdd = !isOdd;
                    return upstream.hasNext();
                }

                @Override
                public U next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    return upstream.next();
                }
            };
        }).asLazy();
    }

    public static <U> LazyIterable<U> collapse(LazyIterable<U> src) {
        return Lists.immutable.withAll(() -> {
            Iterator<U> upstream = src.iterator();
            return new Iterator<U>() {
                U prev = null;
                U curr = null;

                @Override
                public boolean hasNext() {
                    if(curr != null) {
                        return true;
                    }
                    while(upstream.hasNext() && curr == null){
                        U item = upstream.next();
                        if(prev == null || !prev.equals(item)) {
                            curr = item;
                        }
                    }
                    return curr != null;
                }

                @Override
                public U next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    prev = curr;
                    curr = null;
                    return prev;
                }
            };
        }).asLazy();
    }

    @Benchmark
    public long nrOfTransitionsStream(WeatherDataSource src) {
        Stream<String> content = Arrays.stream(src.data)
                .filter(s -> s.charAt(0) != '#') // Filter comments
                .skip(1);                        // Skip line: Not available
        Stream<String> temps = oddLines(content) // Filter hourly info
                .map(line -> line.substring(14, 16));
        return collapse(temps)
                .count();
    }

    @Benchmark
    public long nrOfTransitionsStreamEx(WeatherDataSource src) {
        StreamEx<String> content = StreamEx.of(src.data)
                .filter(s -> s.charAt(0) != '#') // Filter comments
                .skip(1);                        // Skip line: Not available
        StreamEx<String> temps = oddLines(content) // Filter hourly info
                .map(line -> line.substring(14, 16));
        return collapse(temps)
                .count();
    }

    @Benchmark
    public long nrOfTransitionsJayield(WeatherDataSource src) {
        return Query.of(src.data)
                .filter(s -> s.charAt(0) != '#')   // Filter comments
                .skip(1)                           // Skip line: Not available
                .then(QueryNrOfTemperaturesTransitions::oddLines) // Filter hourly info
                .map(line -> line.substring(14, 16))
                .then(QueryNrOfTemperaturesTransitions::collapse)
                .count();
    }

    @Benchmark
    public long nrOfTransitionsJool(WeatherDataSource src) {
        return collapse(
                oddLines(Seq.of(src.data)
                        .filter(s -> s.charAt(0) != '#') // Filter comments
                        .skip(1)  // Skip line: Not available
                )                 // Filter hourly info
                        .map(line -> line.substring(14, 16))
        )
                .count();
    }

    @Benchmark
    public long nrOfTransitionsVavr(WeatherDataSource src) {
        return collapse(
                oddLines(io.vavr.collection.Stream.of(src.data)
                        .filter(s -> s.charAt(0) != '#')// Filter comments
                        .drop(1) // Skip line: Not available
                )                // Filter hourly info
                        .map(line -> line.substring(14, 16))
        )
                .size();
    }

    @Benchmark
    public int nrOfTransitionsKotlin(WeatherDataSource src) {
        Sequence<String> content = drop(
                filter(
                        asSequence(src.data),
                        s -> s.charAt(0) != '#'  // Filter comments
                ),
                1                        // Skip line: Not available
        );
        return count(
                CollapseKt.collapse(
                        map(
                                OddLinesKt.oddLines(content), // Filter hourly info
                                line -> line.substring(14, 16)
                        )
                )
        );
    }

    @Benchmark
    public int nrOfTransitionsKotlinYield(WeatherDataSource src) {
        Sequence<String> content = drop(
                filter(
                        asSequence(src.data),
                        s -> s.charAt(0) != '#'
                ),
                1
        );
        return count(
                YieldCollapseKt.yieldCollapse(
                        map(
                                YieldOddLinesKt.yieldOddLines(content), // Filter hourly info
                                line -> line.substring(14, 16)
                        )
                )
        );
    }

    @Benchmark
    public long nrOfTransitionsEclipse(WeatherDataSource src) {
        LazyIterable<String> content = Lists.immutable.of(src.data).asLazy()
                .select(s -> s.charAt(0) != '#') // Filter comments
                .drop(1);                        // Skip line: Not available
        LazyIterable<String> temps = oddLines(content) // Filter hourly info
                .collect(line -> line.substring(14, 16));
        return collapse(temps)
                .size();
    }

    @Benchmark
    public int nrOfTransitionsSek(WeatherDataSource src) {
        return Sek.of(src.data)
                .filter(s -> s.charAt(0) != '#')
                .drop(1)
                .then(YieldOddLinesKt::yieldOddLines)
                .map(line -> line.substring(14, 16))
                .then(YieldCollapseKt::yieldCollapse)
                .count();
    }

}

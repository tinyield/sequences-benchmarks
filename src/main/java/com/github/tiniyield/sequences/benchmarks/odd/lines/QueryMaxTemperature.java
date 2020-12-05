package com.github.tiniyield.sequences.benchmarks.odd.lines;

import com.github.tiniyield.sequences.benchmarks.common.StreamOddLinesOperation;
import com.github.tiniyield.sequences.benchmarks.common.WeatherDataSource;
import com.github.tiniyield.sequences.benchmarks.kt.odd.lines.OddLinesKt;
import com.github.tiniyield.sequences.benchmarks.kt.odd.lines.YieldOddLinesKt;
import com.tinyield.Sek;
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
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Integer.parseInt;
import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.sequences.SequencesKt.drop;
import static kotlin.sequences.SequencesKt.filter;
import static kotlin.sequences.SequencesKt.map;
import static kotlin.sequences.SequencesKt.maxBy;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.Throughput)
public class QueryMaxTemperature {

    public static <T> StreamEx<T> oddLines(StreamEx<T> source) {
        return source.headTail((__, t) -> t.headTail((head, tail) -> oddLines(tail).prepend(head)));
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

    public static <T> Stream<T> oddLines(Stream<T> source) {
        return StreamSupport.stream(new StreamOddLinesOperation.StreamOddLines<>(source.spliterator()), false);
    }

    public static <T> Seq<T> oddLines(Seq<T> source) {
        return Seq.seq(oddLines(source.stream()));
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

    @Benchmark
    public int maxTempStream(WeatherDataSource src) {
        Stream<String> content = Arrays.stream(src.data)
                .filter(s -> s.charAt(0) != '#') // Filter comments
                .skip(1);                       // Skip line: Not available
        return oddLines(content) // Filter hourly info
                .mapToInt(line -> parseInt(line.substring(14, 16)))
                .max()
                .getAsInt();
    }

    @Benchmark
    public int maxTempStreamEx(WeatherDataSource src) {
        return oddLines(StreamEx.of(src.data)
                .filter(s -> s.charAt(0) != '#')// Filter comments
                .skip(1)                        // Skip line: Not available
        )                                       // Filter hourly info
                .mapToInt(line -> parseInt(line.substring(14, 16)))
                .max()
                .getAsInt();
    }

    @Benchmark
    public int maxTempJayield(WeatherDataSource src) {
        return Query.of(src.data)
                .filter(s -> s.charAt(0) != '#')   // Filter comments
                .skip(1)                           // Skip line: Not available
                .then(QueryMaxTemperature::oddLines) // Filter hourly info
                .mapToInt(line -> parseInt(line.substring(14, 16)))
                .max()
                .getAsInt();
    }

    @Benchmark
    public int maxTempJool(WeatherDataSource src) {
        return oddLines(Seq.of(src.data)
                .filter(s -> s.charAt(0) != '#') // Filter comments
                .skip(1)  // Skip line: Not available
        )                 // Filter hourly info
                .mapToInt(line -> parseInt(line.substring(14, 16)))
                .max()
                .getAsInt();
    }

    @Benchmark
    public int maxTempVavr(WeatherDataSource src) {
        return oddLines(io.vavr.collection.Stream.of(src.data)
                .filter(s -> s.charAt(0) != '#')// Filter comments
                .drop(1) // Skip line: Not available
        )                // Filter hourly info
                .map(line -> parseInt(line.substring(14, 16)))
                .maxBy(Integer::compare)
                .get();
    }

    @Benchmark
    public int maxTempKotlin(WeatherDataSource src) {
        Sequence<String> content = drop(
                filter(
                        asSequence(src.data),
                        s -> s.charAt(0) != '#'  // Filter comments
                ),
                1                        // Skip line: Not available
        );
        return maxBy(
                map(
                        OddLinesKt.oddLines(content), // Filter hourly info
                        line -> parseInt(line.substring(14, 16))
                ),
                i -> i
        );
    }

    @Benchmark
    public int maxTempKotlinYield(WeatherDataSource src) {
        Sequence<String> content = drop(
                filter(
                        asSequence(src.data),
                        s -> s.charAt(0) != '#'  // Filter comments
                ),
                1                        // Skip line: Not available
        );
        return maxBy(
                map(
                        YieldOddLinesKt.yieldOddLines(content), // Filter hourly info
                        line -> parseInt(line.substring(14, 16))
                ),
                i -> i
        );
    }

    @Benchmark
    public int maxTempEclipse(WeatherDataSource src) {
        return oddLines(
                Lists.immutable.of(src.data).asLazy()
                        .select(s -> s.charAt(0) != '#')   // Filter comments
                        .drop(1)                           // Skip line: Not available
        ) // Filter hourly info
                .collectInt(line -> parseInt(line.substring(14, 16)))
                .max();
    }

    @Benchmark
    public int maxTempSek(WeatherDataSource src) {
        return Sek.of(src.data)
                .filter(s -> s.charAt(0) != '#')
                .drop(1)
                .then(YieldOddLinesKt::yieldOddLines)
                .map(line -> parseInt(line.substring(14, 16)))
                .maxByOrNull(i -> i);
    }
}

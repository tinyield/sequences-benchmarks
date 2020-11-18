package com.github.tiniyield.sequences.benchmarks.odd.lines;

import com.github.tiniyield.sequences.benchmarks.kt.odd.lines.OddLinesKt;
import io.vavr.control.Option;
import kotlin.sequences.Sequence;
import one.util.streamex.StreamEx;
import org.jayield.Query;
import org.jayield.Traverser;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.lang.Integer.parseInt;
import static kotlin.collections.ArraysKt.asSequence;
import static kotlin.sequences.SequencesKt.count;
import static kotlin.sequences.SequencesKt.distinct;
import static kotlin.sequences.SequencesKt.drop;
import static kotlin.sequences.SequencesKt.filter;
import static kotlin.sequences.SequencesKt.map;


@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.Throughput)
public class QueryNrOfDistinctTemperatures {

    public static <T> StreamEx<T> oddLines(StreamEx<T> source) {
        return source.headTail((__, t) -> t.headTail((head, tail) -> oddLines(tail).prepend(head)));
    }

    public static <T> io.vavr.collection.Stream<T> oddLines(io.vavr.collection.Stream<T> source) {
        if(source.isEmpty()){
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
        return StreamSupport.stream(
                new StreamOddLinesOperation.StreamOddLines<>(source.spliterator()), false);
    }

    public static <T> Seq<T> oddLines(Seq<T> source) {
        return Seq.seq(oddLines(source.stream()));
    }

    public static <U> Traverser<U> oddLines(Query<U> src) {
        return yield -> {
            final boolean[] isOdd = {false};
            src.traverse(item -> {
                if (isOdd[0]) yield.ret(item);
                isOdd[0] = !isOdd[0];
            });
        };
    }

    @Benchmark
    public long nrOfTempsStream(DataSource src) {
        Stream<String> content = Arrays.stream(src.data)
                .filter(s -> s.charAt(0) != '#') // Filter comments
                .skip(1);                       // Skip line: Not available
        return oddLines(content) // Filter hourly info
                .mapToInt(line -> parseInt(line.substring(14, 16)))
                .distinct()
                .count();
    }

    @Benchmark
    public long nrOfTempsStreamEx(DataSource src) {
        return oddLines(StreamEx.of(src.data)
                .filter(s -> s.charAt(0) != '#')// Filter comments
                .skip(1)                        // Skip line: Not available
        )                                       // Filter hourly info
                .mapToInt(line -> parseInt(line.substring(14, 16)))
                .distinct()
                .count();
    }

    @Benchmark
    public long nrOfTempsJayield(DataSource src) {
        return Query.of(src.data)
                .filter(s -> s.charAt(0) != '#')   // Filter comments
                .skip(1)                           // Skip line: Not available
                .then(QueryMaxTemperature::oddLines) // Filter hourly info
                .mapToInt(line -> parseInt(line.substring(14, 16)))
                .distinct()
                .count();
    }

    @Benchmark
    public long nrOfTempsJool(DataSource src) {
        return oddLines(Seq.of(src.data)
                .filter(s -> s.charAt(0) != '#') // Filter comments
                .skip(1)  // Skip line: Not available
        )                 // Filter hourly info
                .mapToInt(line -> parseInt(line.substring(14, 16)))
                .distinct()
                .count();
    }

    @Benchmark
    public long nrOfTempsVavr(DataSource src) {
        return oddLines(io.vavr.collection.Stream.of(src.data)
                .filter(s -> s.charAt(0) != '#')// Filter comments
                .drop(1) // Skip line: Not available
        )                // Filter hourly info
                .map(line -> parseInt(line.substring(14, 16)))
                .distinct()
                .size();
    }

    @Benchmark
    public long nrOfTempsKotlin(DataSource src) {
        Sequence<String> content = drop(
                filter(
                        asSequence(src.data),
                        s -> s.charAt(0) != '#'  // Filter comments
                ),
                1                        // Skip line: Not available
        );
        return count(
                distinct(
                        map(
                                OddLinesKt.oddLines(content), // Filter hourly info
                                line -> parseInt(line.substring(14, 16))
                        )
                )
        );
    }
}
package benchmarks;

import static benchmarks.SequenceBenchmarkUtils.assertBenchmarkValidity;
import static benchmarks.SequenceBenchmarkUtils.distinctByKey;

import java.util.concurrent.TimeUnit;

import org.javatuples.Triplet;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 10)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
public class SequenceBenchmark {

    @Setup
    public void setup() {
        assertBenchmarkValidity();
    }

    @Benchmark
    public void stream(Blackhole bh) { // With Auxiliary Function
        StreamBenchmark.query().forEach(bh::consume);
    }

    @Benchmark
    public void jayield(Blackhole bh) {
        JayieldBenchmark.query().traverse(bh::consume);
    }

    @Benchmark
    public void streamUtils(Blackhole bh) {
        StreamUtilsBenchmark.query().forEach(bh::consume);
    }

    @Benchmark
    public void guava(Blackhole bh) {
        GuavaBenchmark.query().forEach(bh::consume);
    }


    @Benchmark
    public void streamWithArtistsIterator(Blackhole bh) {
        StreamWithIteratorBenchmark.query().forEach(bh::consume);
    }

}

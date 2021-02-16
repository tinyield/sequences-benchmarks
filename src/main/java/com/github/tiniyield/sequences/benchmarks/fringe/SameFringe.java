package com.github.tiniyield.sequences.benchmarks.fringe;

import com.codepoetics.protonpack.StreamUtils;
import com.github.tiniyield.sequences.benchmarks.common.model.wrapper.Value;
import com.github.tiniyield.sequences.benchmarks.kt.fringe.SameFringeKt;
import com.google.common.collect.Streams;
import com.tinyield.Sek;
import one.util.streamex.StreamEx;
import org.eclipse.collections.api.factory.Lists;
import org.jayield.Advancer;
import org.jayield.Query;
import org.jayield.Traverser;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Spliterators;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.github.tiniyield.sequences.benchmarks.zip.StreamZipOperation.zip;
import static java.util.stream.Stream.generate;
import static kotlin.sequences.SequencesKt.all;
import static kotlin.sequences.SequencesKt.asSequence;
import static kotlin.sequences.SequencesKt.zip;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class SameFringe {

    /**
     * The size of the Sequence for this benchmark
     */
    @Param({"100"})
    public int COLLECTION_SIZE;

    /**
     * lstA and lstB are two Lists with the same Value objects.
     */
    public BinTree<Value> treeA;
    public BinTree<Value> treeB;

    public static Query<Value> getLeavesQuery(BinTree<Value> src) {
        Traverser<Value> trav = yield -> {
            if(src.isLeaf()) yield.ret(src.val);
            else {
                if(src.left != null) getLeavesQuery(src.left).traverse(yield);
                if(src.right != null) getLeavesQuery(src.right).traverse(yield);
            }
        };
        Stack<BinTree<Value>> stack = new Stack<>();
        stack.push(src);
        Advancer<Value> adv = yield -> {
            BinTree<Value> curr = advanceToLeaf(stack);
            if(curr != null) {
                yield.ret(curr.val);
                return true;
            } else
                return false;
        };
        return new Query<>(adv, trav);
    }

    private static <U extends Comparable<U>> BinTree<U> advanceToLeaf(Stack<BinTree<U>> stack) {
        /**
         * Loop finishes when stack is empty or it finds a Leaf.
         */
        while (!stack.isEmpty()) {
            BinTree<U> node = stack.pop();
            if (node.isLeaf())
                return node;
            BinTree<U> rightNode = node.right;
            if (rightNode != null)
                stack.push(rightNode);
            BinTree<U> leftNode = node.left;
            if (leftNode != null)
                stack.push(leftNode);
        }
        return null;
    }

    public static Stream<Value> getLeavesStream(BinTree<Value> src) {
        return StreamSupport
                .stream(Spliterators.spliterator(src.iterator(), -1, 0), false);
    }

    public static StreamEx<Value> getLeavesStreamEx(BinTree<Value> src) {
        return StreamEx.of(src.iterator());
    }

    public static Seq<Value> getLeavesJool(BinTree<Value> src) {
        return Seq.seq(src.iterator());
    }

    public static io.vavr.collection.Stream<Value> getLeavesVavr(BinTree<Value> src) {
        return io.vavr.collection.Stream.ofAll(src);
    }

    public static Stream<Value> getLeavesProtonpack(BinTree<Value> src) {
        return getLeavesStream(src);
    }

    public static Stream<Value> getLeavesGuava(BinTree<Value> src) {
        return getLeavesStream(src);
    }


    /**
     * Sets up the data sources to be used in this benchmark
     */
    @Setup
    public void init() {
        Random rand = new Random(110456);
        Value[] nrs = generate(() -> rand.nextInt(1000))
                .distinct()
                .limit(COLLECTION_SIZE)
                .map(Value::new)
                .toArray(Value[]::new);
        treeA = new BinTree<>(Arrays.stream(nrs));
        treeB = new BinTree<>(Arrays.stream(nrs));
    }

    @Benchmark
    public boolean stream() {
        return zip(getLeavesStream(treeA), getLeavesStream(treeB), (t1, t2) -> t1.compareTo(t2) == 0)
                .allMatch(f -> f);
    }

    @Benchmark
    public boolean streamEx() {
        return getLeavesStreamEx(treeA)
                .zipWith(getLeavesStreamEx(treeB), (t1, t2) -> t1.compareTo(t2) == 0)
                .allMatch(f -> f);
    }

    @Benchmark()
    public boolean jayield() {
        return getLeavesQuery(treeA)
                .zip(getLeavesQuery(treeB), (t1, t2) -> t1.compareTo(t2) == 0)
                .allMatch(f -> f);
    }

    @Benchmark
    public boolean jool() {
        return getLeavesJool(treeA)
                .zip(getLeavesJool(treeB), (t1, t2) -> t1.compareTo(t2) == 0)
                .allMatch(f -> f);
    }

    @Benchmark
    public boolean vavr() {
        return getLeavesVavr(treeA)
                .zipWith(getLeavesVavr(treeB), (t1, t2) -> t1.compareTo(t2) == 0)
                .forAll(f -> f);
    }

    @Benchmark
    public boolean protonpack() {
        return StreamUtils.zip(getLeavesProtonpack(treeA), getLeavesProtonpack(treeB), (t1, t2) -> t1.compareTo(t2) == 0)
                .allMatch(f -> f);
    }

    @Benchmark
    public boolean guava() {
        return Streams.zip(getLeavesGuava(treeA), getLeavesGuava(treeB), (t1, t2) -> t1.compareTo(t2) == 0)
                .allMatch(f -> f);
    }

    @Benchmark
    public boolean zipline() {
        Iterator<Value> it = treeB.iterator();
        return getLeavesStream(treeA)
                .map(t -> t.equals(it.next()))
                .allMatch(Boolean.TRUE::equals);
    }

    @Benchmark
    public boolean kotlin() {
        return SameFringeKt.sameFringe(treeA, treeB, Value::equals);
    }

    @Benchmark
    public boolean jkotlin() {
        return all(zip(SameFringeKt.getLeaves(treeA), SameFringeKt.getLeaves(treeB), Value::equals), Boolean.TRUE::equals);
    }

    @Benchmark
    public boolean eclipse() {
        return Lists.immutable.ofAll(treeA).asLazy()
                .zip(Lists.immutable.ofAll(treeB).asLazy())
                .collect(p -> p.getOne().equals(p.getTwo()))
                .allSatisfy(Boolean.TRUE::equals);
    }

    @Benchmark
    public final boolean sek() {
        Sek<Value> src = SameFringeKt.getLeaves(treeA)::iterator;
        return src
                .zip(SameFringeKt.getLeaves(treeB), Value::equals)
                .all(Boolean.TRUE::equals);
    }
}

package com.github.tiniyield.sequences.benchmarks.zip;

import com.github.tiniyield.sequences.benchmarks.common.model.wrapper.Value;
import kotlin.sequences.SequencesKt;
import org.javatuples.Pair;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class VanillaZipBenchmarkTest {

    private VanillaZipBenchmark instance;
    private List<Pair<Integer, Value>> expected;

    @BeforeMethod
    public void setup() {
        instance = new VanillaZipBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.init();
        expected = List.of(
                new Pair<>(2, new Value(0)),
                new Pair<>(3, new Value(1)),
                new Pair<>(5, new Value(2)),
                new Pair<>(7, new Value(3))
        );
    }

    @Test
    public void testStreamPipeline() {
        List<Pair<Integer, Value>> actual = instance.streamPipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void testStreamExPipeline() {
        List<Pair<Integer, Value>> actual = instance.streamExPipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void testJayieldPipeline() {
        List<Pair<Integer, Value>> actual = instance.jayieldPipeline().toList();

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void testJoolPipeline() {
        List<Pair<Integer, Value>> actual = instance.joolPipeline().toList();

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void testVavrPipeline() {
        List<Pair<Integer, Value>> actual = instance.vavrPipeline().toJavaList();

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void testProtonpackPipeline() {
        List<Pair<Integer, Value>> actual = instance.protonpackPipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void testGuavaPipeline() {
        List<Pair<Integer, Value>> actual = instance.guavaPipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void testZiplinePipeline() {
        List<Pair<Integer, Value>> actual = instance.ziplinePipeline().collect(Collectors.toList());

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void testKotlinPipeline() {
        List<Pair<Integer, Value>> actual = SequencesKt.toList(instance.kotlinPipeline());

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void testJKotlinPipeline() {
        List<Pair<Integer, Value>> actual = SequencesKt.toList(instance.jKotlinPipeline());

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void testEclipsePipeline() {
        List<Pair<Integer, Value>> actual = instance.eclipsePipeline().toList();

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).hasSameElementsAs(expected);
    }

    @Test
    public void testSekPipeline() {
        List<Pair<Integer, Value>> actual = instance.sekPipeline().toList();

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).hasSameElementsAs(expected);
    }

}

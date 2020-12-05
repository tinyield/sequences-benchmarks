package com.github.tiniyield.sequences.benchmarks.all.match;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.github.tiniyield.sequences.benchmarks.common.BenchmarkConstants.EVEN;
import static com.github.tiniyield.sequences.benchmarks.common.BenchmarkConstants.ODD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AllMatchBenchmarkTest {

    private AllMatchBenchmark instance;
    private final List<Integer> expected = List.of(
            EVEN,
            EVEN,
            EVEN,
            EVEN,
            EVEN,
            EVEN,
            EVEN,
            EVEN,
            EVEN,
            EVEN
    );

    @BeforeMethod
    public void setup() {
        instance = new AllMatchBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.setup();
    }

    @Test
    public void testIsEven() {
        assertThat(AllMatchBenchmark.isEven(EVEN)).isTrue();
        assertThat(AllMatchBenchmark.isEven(ODD)).isFalse();
    }

    @Test
    public void testGetAllEvenArray() {
        assertThat(instance.getAllEvenArray()).hasSameElementsAs(expected);
    }

    @Test
    public void testStream() {
        assertThat(instance.stream()).isTrue();
    }

    @Test
    public void testStreamEx() {
        assertThat(instance.streamEx()).isTrue();
    }

    @Test
    public void testJayield() {
        assertThat(instance.jayield()).isTrue();
    }

    @Test
    public void testJool() {
        assertThat(instance.jool()).isTrue();
    }

    @Test
    public void testVavr() {
        assertThat(instance.vavr()).isTrue();
    }

    @Test
    public void testKotlin() {
        assertThat(instance.kotlin()).isTrue();
    }

    @Test
    public void testJkotlin() {
        assertThat(instance.jkotlin()).isTrue();
    }

    @Test
    public void testSek() {
        assertThat(instance.sek()).isTrue();
    }

    @Test
    public void testEclipse() {
        assertThat(instance.eclipse()).isTrue();
    }
}

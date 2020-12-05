package com.github.tiniyield.sequences.benchmarks.first;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static com.github.tiniyield.sequences.benchmarks.common.BenchmarkConstants.EVEN;
import static com.github.tiniyield.sequences.benchmarks.common.BenchmarkConstants.ODD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FindFirstInMiddleBenchmarkTest {

    private final Integer[] expected = {
            EVEN,
            EVEN,
            EVEN,
            EVEN,
            ODD,
            EVEN,
            EVEN,
            EVEN,
            EVEN,
            EVEN,
    };
    private FindFirstInMiddleBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new FindFirstInMiddleBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.init();
    }

    @Test
    public void testGet() {
        Integer[] actual = instance.get();
        assertThat(actual).hasSameElementsAs(Arrays.asList(expected));
    }

    @Test
    public void testInit() {
        assertThat(instance.data).hasSameElementsAs(Arrays.asList(expected));
    }

    @Test
    public void testStream() {
        assertThat(instance.stream()).isEqualTo(ODD);
    }

    @Test
    public void testStreamEx() {
        assertThat(instance.streamEx()).isEqualTo(ODD);
    }

    @Test
    public void testJayield() {
        assertThat(instance.jayield()).isEqualTo(ODD);
    }

    @Test
    public void testJool() {
        assertThat(instance.jool()).isEqualTo(ODD);
    }

    @Test
    public void testVavr() {
        assertThat(instance.vavr()).isEqualTo(ODD);
    }

    @Test
    public void testKotlin() {
        assertThat(instance.kotlin()).isEqualTo(ODD);
    }

    @Test
    public void testJkotlin() {
        assertThat(instance.jkotlin()).isEqualTo(ODD);
    }

    @Test
    public void testEclipse() {
        assertThat(instance.eclipse()).isEqualTo(ODD);
    }

    @Test
    public void testSek() {
        assertThat(instance.sek()).isEqualTo(ODD);
    }
}

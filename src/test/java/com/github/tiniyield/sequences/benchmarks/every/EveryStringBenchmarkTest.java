package com.github.tiniyield.sequences.benchmarks.every;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EveryStringBenchmarkTest {


    private final List<String> expected = List.of(
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10"
    );
    private EveryStringBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new EveryStringBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.init();
    }

    @Test
    public void testInit() {
        assertThat(instance.lstA.toArray()).hasSameElementsAs(expected);
        assertThat(instance.lstB.toArray()).hasSameElementsAs(expected);
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
    public void testProtonpack() {
        assertThat(instance.protonpack()).isTrue();
    }

    @Test
    public void testGuava() {
        assertThat(instance.guava()).isTrue();
    }

    @Test
    public void testZipline() {
        assertThat(instance.zipline()).isTrue();
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
    public void testEclipse() {
        assertThat(instance.eclipse()).isTrue();
    }

    @Test
    public void testSek() {
        assertThat(instance.sek()).isTrue();
    }
}

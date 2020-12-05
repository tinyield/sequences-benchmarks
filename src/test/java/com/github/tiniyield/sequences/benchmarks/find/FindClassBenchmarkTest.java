package com.github.tiniyield.sequences.benchmarks.find;

import com.github.tiniyield.sequences.benchmarks.common.model.wrapper.Value;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FindClassBenchmarkTest {


    private final Value expected = new Value(1);
    private FindClassBenchmark instance;

    @BeforeMethod
    public void setup() {
        instance = new FindClassBenchmark();
        instance.COLLECTION_SIZE = 10;
        instance.init();
        instance.update();
    }

    @Test
    public void testStream() {
        assertThat(instance.stream()).isEqualTo(expected);
    }

    @Test
    public void testStreamEx() {
        assertThat(instance.streamEx()).isEqualTo(expected);
    }

    @Test
    public void testJayield() {
        assertThat(instance.jayield()).isEqualTo(expected);
    }

    @Test
    public void testJool() {
        assertThat(instance.jool()).isEqualTo(expected);
    }

    @Test
    public void testVavr() {
        assertThat(instance.vavr()).isEqualTo(expected);
    }

    @Test
    public void testProtonpack() {
        assertThat(instance.protonpack()).isEqualTo(expected);
    }

    @Test
    public void testGuava() {
        assertThat(instance.guava()).isEqualTo(expected);
    }

    @Test
    public void testZipline() {
        assertThat(instance.zipline()).isEqualTo(expected);
    }

    @Test
    public void testKotlin() {
        assertThat(instance.kotlin()).isEqualTo(expected);
    }

    @Test
    public void testJkotlin() {
        assertThat(instance.jkotlin()).isEqualTo(expected);
    }

    @Test
    public void testEclipse() {
        assertThat(instance.eclipse()).isEqualTo(expected);
    }

    @Test
    public void testSek() {
        assertThat(instance.sek()).isEqualTo(expected);
    }
}

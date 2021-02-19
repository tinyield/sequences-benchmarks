package com.github.tiniyield.sequences.benchmarks.fringe;

import com.github.tiniyield.sequences.benchmarks.common.model.wrapper.Value;
import com.github.tiniyield.sequences.benchmarks.kt.fringe.SameFringeKt;
import kotlin.sequences.SequencesKt;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SameFringeTest {

    private SameFringe instance;

    @BeforeMethod
    public void setup() {
        instance = new SameFringe();
        instance.COLLECTION_SIZE = 1000;
        instance.init();
    }

    @Test
    public void testStream() {
        int [] count = new int[] {0};
        instance.streamPipeline().forEach(v -> count[0]++);
        assertThat(count[0]).isEqualTo(339);
        assertThat(instance.stream()).isTrue();
    }

    @Test
    public void testStreamEx() {
        assertThat(instance.streamExPipeline().count()).isEqualTo(339);
        assertThat(instance.stream()).isTrue();
    }

    @Test
    public void testJayield() {
        assertThat(instance.jayieldPipeline().count()).isEqualTo(339);
        assertThat(instance.jayield()).isTrue();
    }

    @Test
    public void testJool() {
        assertThat(instance.joolPipeline().count()).isEqualTo(339);
        assertThat(instance.jool()).isTrue();
    }

    @Test
    public void testVavr() {
        assertThat(instance.vavrPipeline().size()).isEqualTo(339);
        assertThat(instance.vavr()).isTrue();
    }

    @Test
    public void testProtonpack() {
        assertThat(instance.protonpackPipeline().count()).isEqualTo(339);
        assertThat(instance.protonpack()).isTrue();
    }

    @Test
    public void testGuava() {
        assertThat(instance.guavaPipeline().count()).isEqualTo(339);
        assertThat(instance.guava()).isTrue();
    }

    @Test
    public void testZipline() {
        assertThat(instance.ziplinePipe().count()).isEqualTo(339);
        assertThat(instance.zipline()).isTrue();
    }

    @Test
    public void testKotlin() {
        assertThat(SequencesKt.count(SequencesKt.zip(SameFringeKt.getLeaves(instance.treeA), SameFringeKt.getLeaves(instance.treeB), Value::equals))).isEqualTo(339);
        assertThat(instance.kotlin()).isTrue();
    }

    @Test
    public void testJKotlin() {
        assertThat(SequencesKt.count(instance.jkotlinPipeline())).isEqualTo(339);
        assertThat(instance.jkotlin()).isTrue();
    }

    @Test
    public void testEclipse() {
        assertThat(instance.eclipsePipeline().size()).isEqualTo(339);
        assertThat(instance.eclipse()).isTrue();
    }

    @Test
    public void testSek() {
        assertThat(instance.sekPipeline().count()).isEqualTo(339);
        assertThat(instance.sek()).isTrue();
    }
}

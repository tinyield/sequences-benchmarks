package com.github.tiniyield.sequences.benchmarks;

import com.github.tiniyield.sequences.benchmarks.operations.GuavaOperations;
import com.github.tiniyield.sequences.benchmarks.operations.ProtonpackOperations;
import com.github.tiniyield.sequences.benchmarks.operations.ZiplineOperations;

public abstract class AbstractZipOperationsBenchmark extends AbstractSequenceOperationsBenchmark {

    protected ProtonpackOperations protonpack;
    protected ZiplineOperations zipline;
    protected GuavaOperations guava;

    @Override
    protected void init() {
        super.init();
        protonpack = new ProtonpackOperations();
        zipline = new ZiplineOperations();
        guava = new GuavaOperations();

    }
}

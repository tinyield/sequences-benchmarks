package com.github.tiniyield.sequences.benchmarks;

import com.github.tiniyield.sequences.benchmarks.kt.operations.KotlinOperations;
import com.github.tiniyield.sequences.benchmarks.operations.JoolOperations;
import com.github.tiniyield.sequences.benchmarks.operations.QueryOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamExOperations;
import com.github.tiniyield.sequences.benchmarks.operations.StreamOperations;
import com.github.tiniyield.sequences.benchmarks.operations.VavrOperations;

public abstract class AbstractSequenceOperationsBenchmark {
    protected StreamExOperations streamEx;
    protected StreamOperations stream;
    protected QueryOperations query;
    protected JoolOperations jool;
    protected VavrOperations vavr;
    protected KotlinOperations kotlin;

    protected void init() {
        streamEx = new StreamExOperations();
        stream = new StreamOperations();
        query = new QueryOperations();
        jool = new JoolOperations();
        vavr = new VavrOperations();
        kotlin = new KotlinOperations();
    }
}

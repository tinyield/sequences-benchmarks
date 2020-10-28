package com.github.tiniyield.sequences.benchmarks;

import com.github.tiniyield.sequences.benchmarks.kt.operations.KotlinOperations;
import com.github.tiniyield.sequences.benchmarks.operations.*;

public abstract class AbstractSequenceOperationsBenchmark {
    protected StreamExOperations streamEx;
    protected StreamOperations stream;
    protected QueryOperations query;
    protected JoolOperations jool;
    protected VavrOperations vavr;
    protected KotlinOperations kotlin;
    protected JKotlinOperations jkotlin;

    protected void init() {
        streamEx = new StreamExOperations();
        stream = new StreamOperations();
        query = new QueryOperations();
        jool = new JoolOperations();
        vavr = new VavrOperations();
        kotlin = new KotlinOperations();
        jkotlin = new JKotlinOperations();
    }
}

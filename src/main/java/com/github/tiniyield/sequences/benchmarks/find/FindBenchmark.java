/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.tiniyield.sequences.benchmarks.find;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;

import org.jayield.Query;
import org.jooq.lambda.Seq;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import com.github.tiniyield.sequences.benchmarks.AbstractZipOperationsBenchmark;
import com.github.tiniyield.sequences.benchmarks.IZipBenchmark;

import io.vavr.collection.Stream;
import kotlin.sequences.SequencesKt;
import one.util.streamex.StreamEx;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public abstract class FindBenchmark<T> extends AbstractZipOperationsBenchmark implements IZipBenchmark {

    @Param({"1000"})
    protected int COLLECTION_SIZE;

    protected abstract List<T> getListA();
    protected abstract List<T> getListB();
    protected abstract BiPredicate<T,T> getPredicate();
    protected int index;

    protected abstract void init();

    @Setup()
    public void setupOperations() {
        index = 0;
        super.init();
        init();
    }

    @Setup(Level.Invocation)
    public void iterate() {
        index++;
    }

    @Override
    @Benchmark
    public void stream(Blackhole bh) {
        bh.consume(stream.find(getListA().stream(), getListB().stream(), getPredicate()));
    }

    @Override
    @Benchmark
    public void streamEx(Blackhole bh) {
        bh.consume(streamEx.find(StreamEx.of(getListA()), StreamEx.of(getListB()), getPredicate()));
    }

    @Override
    @Benchmark
    public void jayield(Blackhole bh) {
        bh.consume(query.find(Query.fromList(getListA()), Query.fromList(getListB()), getPredicate()));
    }

    @Override
    @Benchmark
    public void jool(Blackhole bh) {
        bh.consume(jool.find(Seq.seq(getListA()), Seq.seq(getListB()), getPredicate()));
    }

    @Override // could be replaced by corresponds
    @Benchmark
    public void vavr(Blackhole bh) {
        bh.consume(vavr.find(Stream.ofAll(getListA()), Stream.ofAll(getListB()), getPredicate()));
    }


    @Override
    @Benchmark
    public void protonpack(Blackhole bh) {
        bh.consume(protonpack.find(getListA().stream(), getListB().stream(), getPredicate()));
    }

    @Override
    @Benchmark
    public void guava(Blackhole bh) {
        bh.consume(guava.find(getListA().stream(), getListB().stream(), getPredicate()));
    }

    @Override
    @Benchmark
    public void zipline(Blackhole bh) {
        bh.consume(zipline.find(getListA().stream(), getListB().stream(), getPredicate()));
    }

    @Override
    @Benchmark
    public void kotlin(Blackhole bh) {
        bh.consume(kotlin.find(SequencesKt.asSequence(getListA().iterator()), SequencesKt.asSequence(getListB().iterator()), getPredicate()));
    }
}

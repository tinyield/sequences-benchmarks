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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.github.tiniyield.sequences.benchmarks.operations.data.providers.number.IntegerDataProvider;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class FindIntegerBenchmark extends FindBenchmark<Integer> {

    private List<List<Integer>> matrixA;
    private List<List<Integer>> matrixB;


    protected List<Integer> getListA() {
        return matrixA.get(index % COLLECTION_SIZE);
    }


    protected List<Integer> getListB() {
        return matrixB.get(index % COLLECTION_SIZE);
    }


    protected BiPredicate<Integer, Integer> getPredicate() {
        return Integer::equals;
    }


    @Setup
    public void init() {
        matrixA = new ArrayList<>(COLLECTION_SIZE);
        matrixB = new ArrayList<>(COLLECTION_SIZE);
        for (int i = 0; i < COLLECTION_SIZE; i++) {
            matrixA.add(
                    new IntegerDataProvider(COLLECTION_SIZE)
                            .asStream()
                            .collect(Collectors.toList())
            );
            int iFinal = i;
            matrixB.add(
                    new IntegerDataProvider(COLLECTION_SIZE)
                            .asStream()
                            .map(v -> iFinal)
                            .collect(Collectors.toList())
            );
        }
    }

}

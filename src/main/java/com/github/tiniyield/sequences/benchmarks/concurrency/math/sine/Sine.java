package com.github.tiniyield.sequences.benchmarks.concurrency.math.sine;

public class Sine {
    static double slowSin(int value) {
        return FastMathCalc.slowSin(value, null);
    }
}

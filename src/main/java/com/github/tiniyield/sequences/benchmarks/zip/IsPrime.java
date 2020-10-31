package com.github.tiniyield.sequences.benchmarks.zip;

public class IsPrime {
    public static boolean isPrime(Integer value) {
        if (value <= 1) {
            return false;
        }
        if (value <= 3) {
            return true;
        }
        if (value % 2 == 0) {
            return false;
        }
        int i = 3;
        while (i * i <= value) {
            if (value % i == 0) {
                return false;
            }
            i += 2;
        }
        return true;
    }
}

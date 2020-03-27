package com.github.tiniyield.sequences.benchmarks.data.generator;

public class PrimeGenerator {
    private static final int firstPrime = 2;

    public static Integer[] get(int size) {
        Integer[] primes = new Integer[size];
        if (primes.length > 0) {
            primes[0] = firstPrime;
            for (int i = 1; i < primes.length; i++) {
                primes[i] = getNextPrime(primes[i - 1]);
            }
        }
        return primes;
    }

    private static int getNextPrime(int lastPrime) {
        int value = lastPrime + 1;
        while(!isPrime(value)) {
            value++;
        }
        return value;
    }

    private static boolean isPrime(int value) {
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

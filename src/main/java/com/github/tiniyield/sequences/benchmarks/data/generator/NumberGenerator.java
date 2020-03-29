package com.github.tiniyield.sequences.benchmarks.data.generator;


public class NumberGenerator {

    public static Integer[] get(int size) {
        Integer[] numbers = new Integer[size];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i;
        }
        return numbers;
    }

}

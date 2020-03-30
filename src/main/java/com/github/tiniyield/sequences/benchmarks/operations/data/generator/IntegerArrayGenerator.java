package com.github.tiniyield.sequences.benchmarks.operations.data.generator;


import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.EVEN;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.ODD;

import java.util.Arrays;

public class IntegerArrayGenerator {

    public static Integer[] get(int size) {
        Integer[] numbers = new Integer[size];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i;
        }
        return numbers;
    }

    public static Integer[] getAllEven(int size) {
        Integer[] numbers = new Integer[size];
        Arrays.fill(numbers, EVEN);
        return numbers;
    }

    public static Integer[] getAllEvenExceptEnd(int size) {
        Integer[] numbers = getAllEven(size);
        numbers[numbers.length - 1] = ODD;
        return numbers;
    }

    public static Integer[] getAllEvenExceptMiddle(int size) {
        Integer[] numbers = getAllEven(size);
        numbers[(numbers.length / 2) - 1] = ODD;
        return numbers;
    }

    public static Integer[] getAllEvenExceptStart(int size) {
        Integer[] numbers = getAllEven(size);
        numbers[0] = ODD;
        return numbers;
    }


}

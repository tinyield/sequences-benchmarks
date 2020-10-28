package com.github.tiniyield.sequences.benchmarks.operations.data.generator;


import java.util.Arrays;
import java.util.stream.IntStream;

import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.EVEN;
import static com.github.tiniyield.sequences.benchmarks.operations.common.SequenceBenchmarkConstants.ODD;

public class IntegerArrayGenerator {

    public static int[] get(int size) {
        int[] numbers = new int[size];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i;
        }
        return numbers;
    }

    public static int[] getAllEven(int size) {
        int[] numbers = new int[size];
        Arrays.fill(numbers, EVEN);
        return numbers;
    }

    public static int[] getAllEvenExceptEnd(int size) {
        int[] numbers = getAllEven(size);
        numbers[numbers.length - 1] = ODD;
        return numbers;
    }

    public static int[] getAllEvenExceptMiddle(int size) {
        int[] numbers = getAllEven(size);
        numbers[(numbers.length / 2) - 1] = ODD;
        return numbers;
    }

    public static int[] getAllEvenExceptStart(int size) {
        int[] numbers = getAllEven(size);
        numbers[0] = ODD;
        return numbers;
    }

    public static Integer[] box(int[] src) {
        return IntStream.of(src).boxed().toArray(Integer[]::new);
    }


}

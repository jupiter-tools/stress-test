package com.jupiter.tools.stress.test.extension.benchmark;

import com.jupiter.tools.stress.test.extension.benchmark.annotation.EnableTestBenchmark;
import com.jupiter.tools.stress.test.extension.benchmark.annotation.Fast;
import com.jupiter.tools.stress.test.extension.benchmark.annotation.TestBenchmark;
import org.junit.jupiter.api.Assertions;

import java.util.stream.IntStream;

/**
 * Created on 19.08.2018.
 *
 * @author Korovin Anatoliy
 */
@EnableTestBenchmark
class WarmUpTest {

    @Fast
    @TestBenchmark(warmupIterations = 10, measurementIterations = 20)
    void fastStream() {
        // Act
        int sum = IntStream.range(0, 100_000)
                           .boxed()
                           .mapToInt(i -> i)
                           .sum();
        // Asserts
        Assertions.assertEquals(sum, 704982704);
    }

    @TestBenchmark(warmupIterations = 10, measurementIterations = 20)
    void slowStream() {
        // Act
        int sum = IntStream.range(0, 100_000)
                           .boxed()
                           .map(i -> i * 3)
                           .map(i -> i / 3)
                           .map(i -> i * 2)
                           .map(i -> i / 2)
                           .mapToInt(i -> i)
                           .sum();
        // Asserts
        Assertions.assertEquals(sum, 704982704);
    }
}

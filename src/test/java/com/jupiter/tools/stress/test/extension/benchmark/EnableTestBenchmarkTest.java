package com.jupiter.tools.stress.test.extension.benchmark;


import com.jupiter.tools.stress.test.extension.benchmark.annotation.EnableTestBenchmark;
import com.jupiter.tools.stress.test.extension.benchmark.annotation.Fast;
import com.jupiter.tools.stress.test.extension.benchmark.annotation.TestBenchmark;

/**
 * Created on 17.08.2018.
 *
 * @author Korovin Anatoliy
 */
@EnableTestBenchmark
class EnableTestBenchmarkTest {

    @Fast
    @TestBenchmark(measurementIterations = 15, warmupIterations = 10)
    void testFast() throws InterruptedException {
        Thread.sleep(30);
    }

    @TestBenchmark(measurementIterations = 15, warmupIterations = 10)
    void testSlow() throws InterruptedException {
        Thread.sleep(100);
    }
}

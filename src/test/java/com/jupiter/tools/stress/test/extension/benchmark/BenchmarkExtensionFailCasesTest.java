package com.jupiter.tools.stress.test.extension.benchmark;

import com.jupiter.tools.stress.test.extension.benchmark.annotation.EnableTestBenchmark;
import com.jupiter.tools.stress.test.extension.benchmark.annotation.Fast;
import com.jupiter.tools.stress.test.extension.benchmark.annotation.TestBenchmark;
import org.junit.jupiter.api.Test;
import org.junit.platform.testkit.engine.EngineTestKit;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

/**
 * Created on 17.02.2019.
 *
 * @author Korovin Anatoliy
 */
class BenchmarkExtensionFailCasesTest {

    @Test
    void verifyTestFailed() {
        EngineTestKit
                .engine("junit-jupiter")
                .selectors(selectClass(BenchmarkExtensionFailCase.class))
                .execute()
                .containers()
                .assertStatistics(s -> {
                    s.failed(1);
                });
    }

    @Test
    void verifyTestInvocationsCount() throws NoSuchMethodException {

        TestBenchmark fast = BenchmarkExtensionFailCase.class.getDeclaredMethod("testFast")
                                                               .getAnnotation(TestBenchmark.class);

        TestBenchmark slow = BenchmarkExtensionFailCase.class.getDeclaredMethod("testSlow")
                                                               .getAnnotation(TestBenchmark.class);

        EngineTestKit
                .engine("junit-jupiter")
                .selectors(selectClass(BenchmarkExtensionFailCase.class))
                .execute()
                .tests()
                .assertStatistics(stats -> {
                    stats.started(fast.measurementIterations() +
                                  fast.warmupIterations() +
                                  slow.measurementIterations() +
                                  slow.warmupIterations());
                });
    }

    @EnableTestBenchmark
    static class BenchmarkExtensionFailCase {

        @TestBenchmark(measurementIterations = 15, warmupIterations = 10)
        void testFast() throws InterruptedException {
            Thread.sleep(30);
        }

        @Fast
        @TestBenchmark(measurementIterations = 15, warmupIterations = 10)
        void testSlow() throws InterruptedException {
            Thread.sleep(100);
        }
    }

}

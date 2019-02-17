package com.jupiter.tools.stress.test.concurrency.strategy;

import com.jupiter.tools.stress.test.concurrency.ExecutionMode;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 16.02.2019.
 *
 * @author Korovin Anatoliy
 */
class TestRunnerFactoryTest {

    @Test
    void testGetExecutorMode() {
        // Arrange
        TestRunnerFactory testRunnerFactory = new TestRunnerFactory();
        // Act
        TestRunner runner = testRunnerFactory.get(ExecutionMode.EXECUTOR_MODE);
        // Asserts
        assertThat(runner).isInstanceOf(ThreadPoolExecutorTestRunner.class);
    }

    @Test
    void testGetParallelStreamMode() {
        // Arrange
        TestRunnerFactory testRunnerFactory = new TestRunnerFactory();
        // Act
        TestRunner runner = testRunnerFactory.get(ExecutionMode.PARALLEL_STREAM_MODE);
        // Asserts
        assertThat(runner).isInstanceOf(ParallelStreamTestRunner.class);
    }

    @Test
    void testGetUndefined() {
        // Arrange
        TestRunnerFactory testRunnerFactory = new TestRunnerFactory();
        // Act
        Assertions.assertThrows(Exception.class,
                                () -> testRunnerFactory.get(null));
    }
}
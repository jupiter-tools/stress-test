package com.jupiter.tools.stress.test.concurrency;

import com.jupiter.tools.stress.test.concurrency.strategy.TestRunnerFactory;
import com.jupiter.tools.stress.test.concurrency.testrunner.Duration;
import com.jupiter.tools.stress.test.concurrency.testrunner.OneIterationTestResult;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunnerResult;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunnerSettings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.TimeUnit;

import static com.jupiter.tools.stress.test.concurrency.StressTestRunner.DefaultSettings.*;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created on 15.08.2018.
 * <p>
 * The tool for running a test case in concurrency mode.
 * <p>
 * You can use:
 * - an implementation based on the parallel stream
 * or
 * - an implementation based on the {@link java.util.concurrent.ThreadPoolExecutor}
 * <p>
 * For {@link java.util.concurrent.ThreadPoolExecutor} you can set
 * - an available thread number, this implementation provide a more flexible configuration of behavior.
 * <p>
 * Both of these implementation provide a configuration to set a number of iterations.
 * The test case method will be called this number of times,
 * and after that check all results, if any iteration throws an error than
 * {@link StressTestRunner} will throw the {@link AssertionError}.
 *
 * @author Korovin Anatoliy
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StressTestRunner {

    private TestRunnerFactory testRunnerFactory;
    private TestRunnerSettings settings;
    private ExecutionMode mode;

    private StressTestRunner(TestRunnerFactory factory) {
        this.testRunnerFactory = factory;
        this.settings = new TestRunnerSettings(DEFAULT_ITERATIONS,
                                               DEFAULT_THREADS,
                                               new Duration(DEFAULT_TIMEOUT_DURATION,
                                                            DEFAULT_TIMEOUT_UNIT));
        this.mode = DEFAULT_MODE;
    }

    /**
     * factory method
     *
     * @return ConcurrentTestRunner
     */
    public static StressTestRunner test() {
        return new StressTestRunner(new TestRunnerFactory());
    }

    /**
     * Set iteration counter
     */
    public StressTestRunner iterations(int iterations) {
        this.settings.setIterationCount(iterations);
        return this;
    }

    /**
     * Set threads count for EXECUTOR_MODE
     *
     * @param threadsCount count of threads for ThreadPoolTaskExecutor
     */
    public StressTestRunner threads(int threadsCount) {
        this.settings.setThreadCount(threadsCount);
        return this;
    }

    /**
     * Set the mode of concurrently running
     *
     * @param mode ExecutionMode (by stream().parallel() or by ThreadPoolTaskExecutor)
     */
    public StressTestRunner mode(ExecutionMode mode) {
        this.mode = mode;
        return this;
    }

    /**
     * Set a time limit for test execution
     *
     * @param duration after this interval test will fail if not complete
     * @param unit     measure unit for timeout
     */
    public StressTestRunner timeout(int duration, TimeUnit unit) {
        this.settings.setTimeout(new Duration(duration, unit));
        return this;
    }

    /**
     * Run the test case method with selected execution mode
     *
     * @param testCase method that need to run concurrently
     */
    public void run(CallableVoid testCase) {
        // Act
        TestRunnerResult result = testRunnerFactory.get(this.mode)
                                                   .run(testCase, settings);
        // Assert
        Assertions.assertAll(() -> assertThat(result.getErrors()).isEmpty(),
                             () -> assertThat(result.getResults()).containsOnly(OneIterationTestResult.OK));
    }

    static class DefaultSettings {
        static final int DEFAULT_ITERATIONS = 10;
        static final int DEFAULT_THREADS = 4;
        static final ExecutionMode DEFAULT_MODE = ExecutionMode.PARALLEL_STREAM_MODE;
        static final long DEFAULT_TIMEOUT_DURATION = 100;
        static final TimeUnit DEFAULT_TIMEOUT_UNIT = TimeUnit.SECONDS;

        private DefaultSettings() {
        }
    }
}

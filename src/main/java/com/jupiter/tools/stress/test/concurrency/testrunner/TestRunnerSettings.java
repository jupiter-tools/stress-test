package com.jupiter.tools.stress.test.concurrency.testrunner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created on 13.09.2018.
 *
 * @author Korovin Anatoliy
 */
@Getter
@Setter
@AllArgsConstructor
public class TestRunnerSettings {
    /**
     * count of iterations to run test-case
     */
    private int iterationCount;

    /**
     * count of available threads
     */
    private int threadCount;

    /**
     * time limit to test execution
     */
    private Duration timeout;

    /**
     * if true then don't catch uncaught exceptions in other threads
     */
    private boolean dontCatchUncaughtExceptions;
}

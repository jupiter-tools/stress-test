package com.jupiter.tools.stress.test.concurrency.strategy;


import com.jupiter.tools.stress.test.concurrency.ExecutionMode;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunner;

/**
 * Created on 13.09.2018.
 *
 * Factory to provide a different type of TestRunner
 * by the {@link ExecutionMode}
 *
 * @author Korovin Anatoliy
 */
public class TestRunnerFactory {

    public TestRunner get(ExecutionMode mode){
        switch (mode) {
            case PARALLEL_STREAM_MODE:
                return new ParallelStreamTestRunner();
            case TASK_EXECUTOR_MODE:
                return new ThreadPoolExecutorTestRunner();
            default:
                throw new RuntimeException("Not found TestRunner for ["+mode+"] execution mode");
        }
    }
}

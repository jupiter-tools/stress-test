package com.jupiter.tools.stress.test.concurrency;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created on 13.09.2018.
 *
 * Execution Mode of concurrent runner
 *
 * @author Korovin Anatoliy
 */
public enum ExecutionMode {
    /**
     * use a parallel stream to run test case in multithreading mode
     */
    PARALLEL_STREAM_MODE,

    /**
     * use a {@link ThreadPoolExecutor} to run
     * a test case in concurrent mode
     */
    EXECUTOR_MODE
}

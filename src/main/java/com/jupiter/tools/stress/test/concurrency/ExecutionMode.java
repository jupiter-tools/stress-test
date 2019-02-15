package com.jupiter.tools.stress.test.concurrency;

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
     * use a {@link org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor} to run
     * a test case in concurrent mode
     */
    EXECUTOR_MODE
}

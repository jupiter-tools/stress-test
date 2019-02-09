package com.jupiter.tools.stress.test.concurrency.strategy;

import com.jupiter.tools.stress.test.concurrency.CallableVoid;
import com.jupiter.tools.stress.test.concurrency.testrunner.OneIterationTestResult;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunner;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunnerResult;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunnerSettings;
import org.awaitility.Awaitility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created on 13.09.2018.
 * <p>
 * TestRunner implementation based on the {@link ThreadPoolExecutor}.
 * Provides an ability to set a thread count for execute test case in concurrent mode.
 *
 * @author Korovin Anatoliy
 */
public class ThreadPoolExecutorTestRunner implements TestRunner {

    @Override
    public TestRunnerResult run(CallableVoid testCase, TestRunnerSettings settings) {

        List<Throwable> errors = initEmptyErrorList();
        List<Future<OneIterationTestResult>> futureList = new ArrayList<>();
        ThreadPoolExecutor executor = initThreadPool(settings.getThreadCount());

        for (int i = 0; i < settings.getIterationCount(); i++) {
            futureList.add(executor.submit(() -> executeOneIterationResult(testCase, errors)));
        }

        Awaitility.await()
                  .atMost(10, TimeUnit.SECONDS)
                  .until(() -> futureList.stream().allMatch(Future::isDone));

        List<OneIterationTestResult> results = futureList.stream()
                                                         .map(f -> {
                                                             try {
                                                                 return f.get();
                                                             } catch (Exception e) {
                                                                 e.printStackTrace();
                                                                 return OneIterationTestResult.FAIL;
                                                             }
                                                         })
                                                         .collect(Collectors.toList());

        return new TestRunnerResult(errors, results);
    }

    private ThreadPoolExecutor initThreadPool(int threadCount) {

        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(Integer.MAX_VALUE);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threadCount,
                                                             Integer.MAX_VALUE,
                                                             60,
                                                             TimeUnit.SECONDS,
                                                             queue);
        return executor;
    }
}

package com.jupiter.tools.stress.test.concurrency.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.jupiter.tools.stress.test.concurrency.CallableVoid;
import com.jupiter.tools.stress.test.concurrency.testrunner.OneIterationTestResult;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunner;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunnerResult;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunnerSettings;
import org.awaitility.Awaitility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created on 13.09.2018.
 * <p>
 * TestRunner implementation based on the {@link ThreadPoolExecutor}.
 * Provides an ability to set a thread count for execute test case in concurrent mode.
 *
 * @author Korovin Anatoliy
 */
public class ThreadPoolExecutorTestRunner implements TestRunner {

	private Logger log = LoggerFactory.getLogger("stress-test:");

	@Override
	public TestRunnerResult run(CallableVoid testCase, TestRunnerSettings settings) {

		List<Throwable> errors = initEmptyErrorList();
		List<Future<OneIterationTestResult>> futureList = new ArrayList<>();
		ThreadPoolExecutor executor = initThreadPool(settings.getThreadCount());

		for (int i = 0; i < settings.getIterationCount(); i++) {
			futureList.add(executor.submit(() -> executeOneIterationResult(testCase, errors)));
		}

		if (settings.isDontCatchUncaughtExceptions()) {
			Awaitility.await()
			          .dontCatchUncaughtExceptions()
			          .atMost(settings.getTimeout().getDuration(),
			                  settings.getTimeout().getTimeUnit())
			          .until(() -> futureList.stream().allMatch(Future::isDone));
		} else {
			Awaitility.await()
			          .atMost(settings.getTimeout().getDuration(),
			                  settings.getTimeout().getTimeUnit())
			          .until(() -> futureList.stream().allMatch(Future::isDone));
		}

		List<OneIterationTestResult> results = futureList.stream()
		                                                 .map(this::getFutureTestResult)
		                                                 .collect(Collectors.toList());

		return new TestRunnerResult(errors, results);
	}

	private OneIterationTestResult getFutureTestResult(Future<OneIterationTestResult> f) {
		try {
			return f.get();
		} catch (Exception e) {
			log.error("get a result of the iteration failed: ", e);
			return OneIterationTestResult.FAIL;
		}
	}

	private ThreadPoolExecutor initThreadPool(int threadCount) {

		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(Integer.MAX_VALUE);
		return new ThreadPoolExecutor(threadCount,
		                              Integer.MAX_VALUE,
		                              60,
		                              TimeUnit.SECONDS,
		                              queue);
	}
}

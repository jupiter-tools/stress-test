package com.jupiter.tools.stress.test.concurrency;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 15.08.2018.
 *
 * @author Korovin Anatoliy
 */
class StressTestRunnerTest {

	private static final int ITERATIONS = 100000;
	private static final int THREADS = 32;

	@Test
	@DisabledIfEnvironmentVariable(named = "TRAVIS", matches = "true")
	void testConcurrentFailWithoutSync() {
		// Arrange
		NonAtomicInt value = new NonAtomicInt(0);
		// Act
		StressTestRunner.test()
		                .threads(THREADS)
		                .mode(ExecutionMode.EXECUTOR_MODE)
		                .iterations(ITERATIONS)
		                .run(value::increment);
		// Asserts
		assertThat(value.getValue()).isNotEqualTo(ITERATIONS);
	}

	@Test
	@DisabledIfEnvironmentVariable(named = "TRAVIS", matches = "true")
	void testConcurrentFail_toShowFailList() {
		// Arrange
		NonAtomicInt value = new NonAtomicInt(0);

		Error error = Assertions.assertThrows(Error.class, () -> {
			// Act
			StressTestRunner.test()
			                .iterations(ITERATIONS)
			                .run(() -> {
				                int expected = value.getValue() + 1;
				                value.increment();
				                // Assert
				                assertThat(value.getValue()).isEqualTo(expected);
			                });
		});
		// Assert
		assertThat(value.getValue()).isNotEqualTo(ITERATIONS);
		error.printStackTrace();
	}

	@Test
	void testConcurrentWithSync() {
		// Arrange
		NonAtomicInt value = new NonAtomicInt(0);
		// Act
		StressTestRunner.test()
		                .iterations(ITERATIONS)
		                .run(() -> {
			                synchronized (this) {
				                value.increment();
			                }
		                });
		// Asserts
		assertThat(value.getValue()).isEqualTo(ITERATIONS);
	}

	@Test
	@Disabled("only to debug")
	void thr() {
		StressTestRunner.test()
		                .mode(ExecutionMode.EXECUTOR_MODE)
		                .threads(THREADS)
		                .iterations(ITERATIONS)
		                .run(() -> System.out.println(Thread.currentThread().getName()));
	}

	@Test
	void testTimeoutExecutorMode() {

		StressTestRunner.test()
		                .iterations(1)
		                .threads(1)
		                .mode(ExecutionMode.EXECUTOR_MODE)
		                .timeout(5, TimeUnit.SECONDS)
		                .run(() -> {
			                System.out.println("RUN");
			                Thread.sleep(1000);
		                });
	}

	@Test
	void testTimeoutExecutorModeError() {

		Assertions.assertThrows(Exception.class, () -> {

			StressTestRunner.test()
			                .iterations(1)
			                .threads(1)
			                .mode(ExecutionMode.EXECUTOR_MODE)
			                .timeout(1, TimeUnit.SECONDS)
			                .run(() -> {
				                System.out.println("RUN");
				                Thread.sleep(2000);
			                });
		});
	}

	@Test
	void testTimeoutParallelMode() {

		StressTestRunner.test()
		                .iterations(1)
		                .threads(1)
		                .mode(ExecutionMode.PARALLEL_STREAM_MODE)
		                .timeout(5, TimeUnit.SECONDS)
		                .run(() -> {
			                System.out.println("RUN");
			                Thread.sleep(1000);
		                });
	}

	@Test
	void testTimeoutParallelModeError() {

		Assertions.assertThrows(Exception.class,
		                        () -> {
			                        StressTestRunner.test()
			                                        .iterations(10)
			                                        .threads(1)
			                                        .mode(ExecutionMode.PARALLEL_STREAM_MODE)
			                                        .timeout(1, TimeUnit.SECONDS)
			                                        .run(() -> {
				                                        System.out.println("RUN");
				                                        Thread.sleep(1000);
			                                        });
		                        });
	}

	@Test
	void failInTestParallelMode() {
		Assertions.assertThrows(Error.class,
		                        () -> StressTestRunner.test()
		                                              .mode(ExecutionMode.PARALLEL_STREAM_MODE)
		                                              .iterations(1)
		                                              .run(() -> {
			                                              throw new RuntimeException("oops");
		                                              }));
	}

	@Test
	void failInTestExecutorMode() {
		Assertions.assertThrows(Error.class,
		                        () -> StressTestRunner.test()
		                                              .mode(ExecutionMode.EXECUTOR_MODE)
		                                              .iterations(1)
		                                              .run(() -> {
			                                              throw new RuntimeException("oops");
		                                              }));
	}

	@Test
	void testDontCatchUncaughtExceptions() throws InterruptedException {

		AtomicInteger counter = new AtomicInteger(0);

		StressTestRunner.test()
		                .mode(ExecutionMode.EXECUTOR_MODE)
		                .dontCatchUncaughtExceptions()
		                .threads(2)
		                .iterations(20)
		                .run(() -> {
			                new Thread(() -> {
				                throw new RuntimeException(Thread.currentThread().getName() + "-oops");
			                }).start();
			                Thread.sleep(100);
			                counter.incrementAndGet();
		                });

		assertThat(counter.get()).isEqualTo(20);
	}
}
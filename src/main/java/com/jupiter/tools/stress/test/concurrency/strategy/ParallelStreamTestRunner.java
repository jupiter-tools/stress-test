package com.jupiter.tools.stress.test.concurrency.strategy;


import com.jupiter.tools.stress.test.concurrency.CallableVoid;
import com.jupiter.tools.stress.test.concurrency.testrunner.OneIterationTestResult;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunner;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunnerResult;
import com.jupiter.tools.stress.test.concurrency.testrunner.TestRunnerSettings;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created on 13.09.2018.
 *
 * TestRunner implementation based on the parallel stream
 *
 * @author Korovin Anatoliy
 */
public class ParallelStreamTestRunner implements TestRunner {

    @SuppressWarnings("Duplicates")
    @Override
    public TestRunnerResult run(CallableVoid testCase, TestRunnerSettings settings) {
        // Arrange
        List<Throwable> errors = initEmptyErrorList();

        List<OneIterationTestResult> results =
                IntStream.range(0, settings.getIterationCount())
                         .boxed()
                         .parallel()
                         .map(i -> executeOneIterationResult(testCase, errors))
                         .collect(Collectors.toList());

        return new TestRunnerResult(errors, results);
    }
}

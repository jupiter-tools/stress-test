package com.jupiter.tools.stress.test.concurrency.testrunner;

import com.jupiter.tools.stress.test.concurrency.CallableVoid;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 13.09.2018.
 *
 * @author Korovin Anatoliy
 */
public interface TestRunner {

    /**
     * Run test-case in concurrent mode with selected settings.
     *
     * @param testCase function with a test-case
     * @param settings needed settings (concurrency, iterations count, e.t.c)
     *
     * @return result of execution a test-case in concurrent mode.
     */
    TestRunnerResult run(CallableVoid testCase, TestRunnerSettings settings);

    /**
     * init thread-safe empty error list
     */
    default List<Throwable> initEmptyErrorList() {
        return Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * logic of one iteration execute
     *
     * @param testCase test-case
     * @param errors   list to store collection of errors (if they happened while test running)
     *
     * @return result of execution the test-case
     */
    default OneIterationTestResult executeOneIterationResult(CallableVoid testCase,
                                                             List<Throwable> errors) {
        try {
            testCase.call();
            return OneIterationTestResult.OK;
        } catch (Throwable t) {
            LoggerFactory.getLogger("stress-test:")
                         .error("error while execute one iteration", t);
            errors.add(t);
            return OneIterationTestResult.FAIL;
        }
    }
}

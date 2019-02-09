package com.jupiter.tools.stress.test.extension.benchmark;

import com.jupiter.tools.stress.test.extension.benchmark.annotation.EnableTestProfiling;
import com.jupiter.tools.stress.test.extension.benchmark.annotation.MeasureUnit;
import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 17.08.2018.
 * <p>
 * Profiler extension
 * - show results in a console
 * - append results of profiling in the junit5 context storage
 *
 * @author Korovin Anatoliy
 */
public class ProfilerExtension implements AfterAllCallback, BeforeEachCallback, AfterEachCallback, BeforeAllCallback {

    /**
     * Namespace for the storage of test results
     */
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
            .create("com", "antkorwin", "profiler");

    private static Logger log = LoggerFactory.getLogger("profiler-extension:");

    /**
     * return a result of profiling
     * for a current test class in the context
     *
     * @param context test execution context
     *
     * @return map where key is a test method name and value is a test timing result for this method
     */
    public static Map<String, TestTiming> getProfilerResult(ExtensionContext context) {
        return getProfilerResult(context, context.getRequiredTestClass().getName());
    }

    /**
     * return a result of profiling
     * for the testClassName
     *
     * @param context       test execution context
     * @param testClassName name of the class which results of profiling will return
     *
     * @return map where key is a test method name and value is a test timing result for this method
     */
    public static Map<String, TestTiming> getProfilerResult(ExtensionContext context, String testClassName) {
        ExtensionContext.Store store = context.getRoot().getStore(NAMESPACE);
        return (Map<String, TestTiming>) store.get(testClassName);
    }

    /**
     * return a timing of the profiling result (by method name)
     * for current test class in the context.
     *
     * @param context        test execution context
     * @param testMethodName name og the test method
     *
     * @return test timing result for this method
     */
    public static TestTiming getTestTiming(ExtensionContext context, String testMethodName) {
        return getProfilerResult(context).get(testMethodName);
    }

    /**
     * return a timing of the profiling result
     * by test class name
     * and test method name from this class.
     *
     * @param context        test execution context
     * @param testClassName  name of the class which results of profiling will return
     * @param testMethodName name og the test method
     *
     * @return test timing result for this method
     */
    public static TestTiming getTestTiming(ExtensionContext context, String testClassName, String testMethodName) {
        return getProfilerResult(context, testClassName).get(testMethodName);
    }

    /**
     * print profiling results in the console
     *
     * @param results     map with test timing results
     * @param measureUnit measure unit
     */
    public static void printProfilerResult(Map<String, TestTiming> results, TimeUnit measureUnit) {

        results.forEach((method, timing) -> {
            log.info("{} : {} {} ",
                     method,
                     convertTime(measureUnit, timing.getDuration()),
                     measureUnit.name());
        });
    }

    private static long convertTime(TimeUnit unit, double duration) {
        return unit.convert((long) duration, unit);
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

        Map<String, TestTiming> map = getOrCreateProfilerResults(context);

        String testMethodName = context.getRequiredTestMethod().getName();
        map.put(testMethodName, new TestTiming(getCurrentTime(context), evaluateMeasureUnit(context)));
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {

        double endTime = getCurrentTime(context);

        String testMethodName = context.getRequiredTestMethod().getName();
        TestTiming testTiming = getTestTiming(context, testMethodName);
        testTiming.setDuration(endTime - testTiming.getStartTime());
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {

        Map<String, TestTiming> results = getProfilerResult(context);
        printProfilerResult(results, evaluateMeasureUnit(context));
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        assertThat(context.getRequiredTestClass().getAnnotation(EnableTestProfiling.class))
                .describedAs("Not found EnableTestProfiling annotation.")
                .isNotNull();
    }

    private Map<String, TestTiming> getOrCreateProfilerResults(ExtensionContext context) {
        ExtensionContext.Store store = getStore(context);
        return (Map<String, TestTiming>) store.getOrComputeIfAbsent(context.getRequiredTestClass().getName(),
                                                                    k -> new HashMap());
    }

    private ExtensionContext.Store getStore(ExtensionContext context) {
        return context.getRoot().getStore(NAMESPACE);
    }

    private long getCurrentTime(ExtensionContext context) {

        switch (evaluateMeasureUnit(context)) {
            case NANOSECONDS:
                return System.nanoTime();
            default:
                return evaluateMeasureUnit(context).convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
    }

    private TimeUnit evaluateMeasureUnit(ExtensionContext context) {

        return Optional.ofNullable(context.getRequiredTestClass()
                                          .getAnnotation(MeasureUnit.class))
                       .map(MeasureUnit::unit)
                       .orElse(TimeUnit.MILLISECONDS);
    }
}

package com.jupiter.tools.stress.test.extension.benchmark;

import com.jupiter.tools.stress.test.extension.benchmark.annotation.Fast;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 19.08.2018.
 * <p>
 * Extension for make a decision which test-method a fastest in test suite.
 *
 * @author Korovin Anatoliy
 */
public class BenchmarkExtension implements BeforeAllCallback, AfterAllCallback {

    private Logger log = LoggerFactory.getLogger("benchmark-extension:");

    @Override
    public void afterAll(ExtensionContext context) throws Exception {

        // evaluate averages:
        Map<String, List<Double>> iterationResults = getResultOfEachIteration(context);
        iterationResults.forEach((method, result) -> {

            double average = result.stream()
                                   .mapToDouble(d -> d)
                                   .average()
                                   .orElseThrow(() -> new AssertionError("Not found data for test iterations"));

            TestTiming timing = ProfilerExtension.getProfilerResult(context)
                                                 .get(method);
            timing.setAverage(average);
            timing.setDuration(average);
            printAverage(method, timing);
        });

        // check fastest result:
        double expectedResult = getExpectedFasterResult(context);
        ProfilerExtension.getProfilerResult(context).forEach((method, timing) -> {
            if (timing.getDuration() < expectedResult) {
                String fastestName = getExpectedFasterMethodName(context);

                Assertions.fail("\n\nThe test method \"" + fastestName + "\" - is not fastest in this test suite.\n" +
                                "Timing of the method \"" + method + "\" (" + timing.getDuration() + " " + timing
                                        .getTimeUnit().name() + ")" +
                                " less than timing (" + expectedResult + " " + timing.getTimeUnit()
                                                                                     .name() + ") of the expected method \""
                                + fastestName + "\".\n");
            }
        });
    }


    @Override
    public void beforeAll(ExtensionContext context) throws Exception {

        Method[] methods = context.getTestClass()
                                  .map(Class::getDeclaredMethods)
                                  .orElseThrow(() -> new AssertionError("Not found any methods for test"));

        long count = Arrays.stream(methods)
                           .filter(m -> m.isAnnotationPresent(Fast.class))
                           .count();
        assertThat(count)
                .as("Expected one method with the annotation @Fast")
                .isEqualTo(1);
    }


    private double getExpectedFasterResult(ExtensionContext context) {

        return Optional.ofNullable(ProfilerExtension.getProfilerResult(context))
                       .map(r -> r.get(getExpectedFasterMethodName(context)))
                       .map(TestTiming::getDuration)
                       .orElseThrow(() -> getNotFoundError(context));
    }

    private void printAverage(String method, TestTiming timing) {

        log.info("The average time of [{}] = {} {}.",
                 method,
                 timing.getAverage(),
                 timing.getTimeUnit().name());

        if (timing.getAverage() < 1) {
            log.warn("The average time of [{}] is less than one {}, your benchmark may be incorrect.",
                     method,
                     timing.getTimeUnit().name());
        }
    }

    private String getExpectedFasterMethodName(ExtensionContext context) {

        Method[] methods = context.getTestClass()
                                  .map(Class::getDeclaredMethods)
                                  .orElseThrow(() -> new AssertionError("Not found methods for test"));

        Method method = Arrays.stream(methods)
                              .filter(m -> m.isAnnotationPresent(Fast.class))
                              .findFirst()
                              .orElseThrow(() -> new AssertionError(
                                      "Fast method not found, please annotate expected fastest method by annotation Fast"));

        return method.getName();
    }

    private AssertionError getNotFoundError(ExtensionContext context) {
        return new AssertionError("Not found a result for the expected method: " +
                                  getExpectedFasterMethodName(context));
    }

    @SuppressWarnings("unchecked")
    private Map<String, List<Double>> getResultOfEachIteration(ExtensionContext context) {
        Map<String, List<Double>> result = (Map<String, List<Double>>) context.getRoot()
                                                                              .getStore(ProfilerExtension.NAMESPACE)
                                                                              .get(context.getRequiredTestClass()
                                                                                          .getName() + "_iterations");

        if (result == null || result.isEmpty()) {
            Assertions.fail("Not found a benchmark results after execution of test [" +
                            context.getRequiredTestClass() + "] \nit may happened because:" +
                            "\n - the test class didn't use a @TestBenchmark annotation" +
                            "\n - @TestBenchmark used with an iteration setting is less than one");
        }

        return result;
    }

}

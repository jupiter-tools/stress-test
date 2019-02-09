package com.jupiter.tools.stress.test.extension.benchmark;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 22.08.2018.
 *
 * @author Korovin Anatoliy
 */
public class WarmUpAndAverageResultProcessorExtension implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) throws Exception {

        if (isWarmUp(context)) return;

        addCurrentResultInContext(context,
                                  ProfilerExtension.getTestTiming(context, getTestMethodName(context)));
    }

    private void addCurrentResultInContext(ExtensionContext context, TestTiming result) {

        Map<String, List<Double>> results = getOrCreateResultsOfIterationsForTestClass(context);

        List<Double> resultsForTestMethod = results.computeIfAbsent(getTestMethodName(context),
                                                                  k -> new ArrayList<>());

        resultsForTestMethod.add(result.getDuration());
    }

    private boolean isWarmUp(ExtensionContext context) {
        return context.getDisplayName().equals(BenchmarkTestTemplateProvider.WARMUP_TEST_NAME);
    }

    private String getTestMethodName(ExtensionContext context) {
        return context.getRequiredTestMethod().getName();
    }

    @SuppressWarnings("unchecked")
    private Map<String, List<Double>> getOrCreateResultsOfIterationsForTestClass(ExtensionContext context) {

        String testClassName = context.getRequiredTestClass().getName() + "_iterations";

        return (Map<String, List<Double>>) context.getRoot()
                                                .getStore(ProfilerExtension.NAMESPACE)
                                                .getOrComputeIfAbsent(testClassName,
                                                                      name -> new HashMap<String, List<Double>>());
    }
}

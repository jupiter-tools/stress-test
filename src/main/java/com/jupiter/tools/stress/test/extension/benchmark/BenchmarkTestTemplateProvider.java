package com.jupiter.tools.stress.test.extension.benchmark;

import com.jupiter.tools.stress.test.extension.benchmark.annotation.TestBenchmark;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created on 19.08.2018.
 *
 * Template provider for make a sequence of warm-up iteration
 * and sequence of iteration that influence on the test result.
 *
 * @author Korovin Anatoliy
 */
public class BenchmarkTestTemplateProvider implements TestTemplateInvocationContextProvider {


    @Override
    public boolean supportsTestTemplate(ExtensionContext context) {

        boolean annotationPresent = context.getRequiredTestMethod().isAnnotationPresent(TestBenchmark.class);

        assertThat(annotationPresent)
                .as("Not found TestBenchmark annotation for this test method.")
                .isTrue();

        return annotationPresent;
    }

    @Override
    public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext context) {

        TestBenchmark annotation = context.getRequiredTestMethod().getAnnotation(TestBenchmark.class);

        ArrayList<TestTemplateInvocationContext> invocations = new ArrayList<>();

        IntStream.range(0, annotation.warmupIterations())
                 .boxed()
                 .forEach(i -> invocations.add(invocationContext(WARMUP_TEST_NAME)));

        IntStream.range(0, annotation.measurementIterations())
                 .boxed()
                 .forEach(i -> invocations.add(invocationContext(getProcessedIterationName(context))));

        return invocations.stream();
    }


    public static final String WARMUP_TEST_NAME = "🔥warmup";

    private String getProcessedIterationName(ExtensionContext context) {
        return "🔍" + context.getRequiredTestMethod().getName();
    }


    private TestTemplateInvocationContext invocationContext(String parameter) {


        return new TestTemplateInvocationContext() {

            @Override
            public String getDisplayName(int invocationIndex) {
                return parameter;
            }

            @Override
            public List<Extension> getAdditionalExtensions() {

                return Arrays.asList(
                        new WarmUpAndAverageResultProcessorExtension(),
                        new ProfilerExtension());
            }
        };
    }
}

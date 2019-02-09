package com.jupiter.tools.stress.test.extension.benchmark.annotation;

import com.jupiter.tools.stress.test.extension.benchmark.BenchmarkTestTemplateProvider;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 19.08.2018.
 *
 * Use on a test method to run it instrumented by profiler,
 * and check measurement timings after test execution.
 *
 * @author Korovin Anatoliy
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@TestTemplate
@ExtendWith(BenchmarkTestTemplateProvider.class)
public @interface TestBenchmark {

    /**
     * count of measurement iterations,
     * a fastest method will be select by the average of these times.
     *
     * @return
     */
    int measurementIterations();

    /**
     * the number of warm-up iterations,
     * which runs before starts a sequence of measurements.
     */
    int warmupIterations() default 0;
}

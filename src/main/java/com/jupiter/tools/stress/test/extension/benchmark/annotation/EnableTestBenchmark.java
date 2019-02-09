package com.jupiter.tools.stress.test.extension.benchmark.annotation;

import com.jupiter.tools.stress.test.extension.benchmark.BenchmarkExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 17.08.2018.
 *
 * Runs a test suite and asserts that the execution time
 * of each test does not better than the time of the fastest method
 * (set by this annotation)
 *
 * @author Korovin Anatoliy
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(BenchmarkExtension.class)
public @interface EnableTestBenchmark {

}

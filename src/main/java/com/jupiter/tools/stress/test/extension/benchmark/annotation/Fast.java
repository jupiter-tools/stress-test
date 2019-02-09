package com.jupiter.tools.stress.test.extension.benchmark.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 19.08.2018.
 *
 * Mark the fastest method in a test suite by this annotation,
 * use with TestBenchmark annotation.
 *
 * @author Korovin Anatoliy
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Fast {
}

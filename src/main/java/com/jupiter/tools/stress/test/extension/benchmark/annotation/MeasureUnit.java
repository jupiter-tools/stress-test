package com.jupiter.tools.stress.test.extension.benchmark.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Created on 17.08.2018.
 *
 * This annotation provides an ability to set a unit of measure,
 * which will use in the ProfilerExtension and BenchmarkExtension.
 *
 * If you need to profile a very fast process then you can set
 * a MeasureUnit to NANOSECONDS, after that the ProfilerExtension
 * will use the System.nanoTime() to measure duration.
 *
 * By default, ProfilerExtension use a MILLISECONDS setting
 *
 * @author Korovin Anatoliy
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MeasureUnit {

    TimeUnit unit() default TimeUnit.MILLISECONDS;
}

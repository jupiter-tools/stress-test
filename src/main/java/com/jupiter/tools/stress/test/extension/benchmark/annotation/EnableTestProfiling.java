package com.jupiter.tools.stress.test.extension.benchmark.annotation;

import com.jupiter.tools.stress.test.extension.benchmark.ProfilerExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 17.08.2018.
 *
 * Turn ON profiler for tests
 * and show results in a console
 * also append results of profiling in the junit5 context storage
 *
 * @author Korovin Anatoliy
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(ProfilerExtension.class)
public @interface EnableTestProfiling {

}

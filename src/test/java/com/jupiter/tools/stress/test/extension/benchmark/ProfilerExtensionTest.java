package com.jupiter.tools.stress.test.extension.benchmark;

import com.jupiter.tools.stress.test.extension.benchmark.annotation.EnableTestProfiling;
import org.junit.jupiter.api.Test;

/**
 * Created on 18.08.2018.
 *
 * @author Korovin Anatoliy
 */
@EnableTestProfiling
class ProfilerExtensionTest {

    @Test
    void first() throws InterruptedException {
        Thread.sleep(10);
    }
}

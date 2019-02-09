package com.jupiter.tools.stress.test.extension.benchmark.measureunit;


import com.jupiter.tools.stress.test.extension.benchmark.ProfilerExtension;
import com.jupiter.tools.stress.test.extension.benchmark.annotation.EnableTestProfiling;
import com.jupiter.tools.stress.test.extension.benchmark.annotation.MeasureUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

/**
 * Created on 18.08.2018.
 *
 * @author Korovin Anatoliy
 */
@EnableTestProfiling
@ExtendWith({ProfilerExtension.class, ProfilerExtensionMeasureUnitSecondsTest.TestExtension.class})
@MeasureUnit(unit = TimeUnit.SECONDS)
class ProfilerExtensionMeasureUnitSecondsTest {

    private static long TIMEOUT = 2;
    private static long EXPECTED_THRESHOLD = 1;
    private static final String TEST_METHOD_NAME = "checkMeasureUnit";

    @Test
    void checkMeasureUnit() throws InterruptedException {
        Thread.sleep(TimeUnit.SECONDS.toMillis(TIMEOUT));
    }

    static class TestExtension extends BaseMeasureTestExtension {

        @Override
        long getExpectedThreshold() {
            return EXPECTED_THRESHOLD;
        }

        @Override
        long getTimeout() {
            return TIMEOUT;
        }

        @Override
        String getTestMethodName() {
            return TEST_METHOD_NAME;
        }

        @Override
        long getCurrentTime() {
            return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        }
    }
}

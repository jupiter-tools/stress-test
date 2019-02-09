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
@ExtendWith({ProfilerExtension.class, ProfilerExtensionMeasureUnitMillisTest.TestExtension.class})
@MeasureUnit(unit = TimeUnit.MILLISECONDS)
class ProfilerExtensionMeasureUnitMillisTest {

    private static long TIMEOUT = 500;
    private static long EXPECTED_THRESHOLD = TIMEOUT / 2;
    private static final String TEST_METHOD_NAME = "checkMillisecondsMeasureUnit";

    @Test
    void checkMillisecondsMeasureUnit() throws InterruptedException {
        Thread.sleep(TIMEOUT);
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
            return System.currentTimeMillis();
        }
    }
}

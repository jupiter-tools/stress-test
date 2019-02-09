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
@ExtendWith({ProfilerExtension.class, ProfilerExtensionMeasureUnitNanosTest.TestExtension.class})
@MeasureUnit(unit = TimeUnit.NANOSECONDS)
class ProfilerExtensionMeasureUnitNanosTest {

    private static long TIMEOUT = 500;
    private static long EXPECTED_THRESHOLD = TIMEOUT / 2;
    private static final String TEST_METHOD_NAME = "checkNanosecondsMeasureUnit";

    @Test
    void checkNanosecondsMeasureUnit() throws InterruptedException {
        Thread.sleep(TIMEOUT);
    }

    static class TestExtension extends BaseMeasureTestExtension {

        @Override
        long getExpectedThreshold() {
            return TimeUnit.MILLISECONDS.toNanos(EXPECTED_THRESHOLD);
        }

        @Override
        long getTimeout() {
            return TimeUnit.MILLISECONDS.toNanos(TIMEOUT);
        }

        @Override
        String getTestMethodName() {
            return TEST_METHOD_NAME;
        }

        @Override
        long getCurrentTime() {
            return System.nanoTime();
        }
    }
}

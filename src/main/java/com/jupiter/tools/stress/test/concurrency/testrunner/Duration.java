package com.jupiter.tools.stress.test.concurrency.testrunner;

import java.util.concurrent.TimeUnit;

/**
 * Created on 15.02.2019.
 *
 * @author Korovin Anatoliy
 */
public class Duration {

    private long duration;
    private TimeUnit timeUnit;

    public Duration(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    public long getDuration() {
        return duration;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}

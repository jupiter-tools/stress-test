package com.jupiter.tools.stress.test.extension.benchmark;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created on 19.08.2018.
 *
 * A single result of measuring the duration of the test method.
 *
 * @author Korovin Anatoliy
 */
@Getter
@Setter
public class TestTiming {

    private long startTime;
    private double duration;
    private double average;
    private TimeUnit timeUnit;
    private List<Long> results = new ArrayList<>();

    public TestTiming(long startTime, TimeUnit timeUnit) {
        this.startTime = startTime;
        this.timeUnit = timeUnit;
    }
}

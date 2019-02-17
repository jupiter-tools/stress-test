package com.jupiter.tools.stress.test.concurrency.testrunner;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * Created on 15.02.2019.
 *
 * @author Korovin Anatoliy
 */
@Getter
@AllArgsConstructor
public class Duration {

    private long duration;
    private TimeUnit timeUnit;
}

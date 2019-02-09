package com.jupiter.tools.stress.test.concurrency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created on 05.07.2018.
 * <p>
 * Not tread-safe integer value class.
 * Use it just for testing.
 *
 * @author Korovin Anatoliy
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NonAtomicInt {
    private int value;

    public int increment() {
        return ++value;
    }
}
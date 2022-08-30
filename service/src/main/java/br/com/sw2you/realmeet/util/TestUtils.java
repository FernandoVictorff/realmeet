package br.com.sw2you.realmeet.util;

import java.util.concurrent.TimeUnit;

public final class TestUtils {

    private TestUtils() {}

    public static void sleep(long millis) {
        try {
            TimeUnit.MICROSECONDS.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

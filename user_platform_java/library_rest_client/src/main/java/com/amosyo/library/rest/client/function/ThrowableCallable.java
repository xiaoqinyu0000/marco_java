package com.amosyo.library.rest.client.function;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public interface ThrowableCallable<T> {

    Logger logger = Logger.getLogger(ThrowableCallable.class.getName());

    T call() throws Throwable;

    static <T> T call(ThrowableCallable<T> callable, final boolean ignoredException, final T defaultValue) throws RuntimeException {
        try {
            return callable.call();
        } catch (Throwable e) {
            if (ignoredException) {
                logger.log(Level.WARNING, "ignoredException", e);
                return defaultValue;
            } else {
                throw new RuntimeException(e);
            }
        }
    }
}

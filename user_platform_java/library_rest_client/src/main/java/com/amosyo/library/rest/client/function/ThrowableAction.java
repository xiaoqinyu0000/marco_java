package com.amosyo.library.rest.client.function;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public interface ThrowableAction {

    void execute() throws Throwable;

    static void execute(ThrowableAction action) throws RuntimeException {
        try {
            action.execute();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}

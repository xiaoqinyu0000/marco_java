package com.amosyo.configure;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.eclipse.microprofile.config.Config;

import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class ConfigFace {

    public static ThreadLocal<Config> mConfigThreadLocal = new ThreadLocal<>();

    public static Config getConfig() {
        return mConfigThreadLocal.get();
    }

    public static void register(@NonNull final Config config) {
        mConfigThreadLocal.set(requireNonNull(config, "config"));
    }

    public static void unRegister() {
        mConfigThreadLocal.remove();
    }
}

package com.amosyo.application.user.projects.user.service;

import com.amosyo.application.user.projects.user.bo.UserBO;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public interface UserService {

    void register(final @NonNull UserBO user);
}

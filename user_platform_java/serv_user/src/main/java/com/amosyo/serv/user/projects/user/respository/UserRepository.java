package com.amosyo.serv.user.projects.user.respository;

import com.amosyo.serv.user.projects.user.domain.User;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public interface UserRepository {

    void save(@NonNull final User user);
}

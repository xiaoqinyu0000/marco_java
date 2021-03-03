package com.amosyo.serv.user.projects.user.service.impl;

import com.amosyo.serv.user.projects.user.domain.User;
import com.amosyo.serv.user.projects.user.respository.DbUserRepository;
import com.amosyo.serv.user.projects.user.respository.UserRepository;
import com.amosyo.serv.user.projects.user.service.UserService;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl() {
        this.userRepository = new DbUserRepository();
    }

    @Override
    public void register(@NonNull final User user) {
        userRepository.save(user);
    }
}

package com.amosyo.serv.user.projects.user.service.impl;

import com.amosyo.serv.user.projects.user.bo.UserBO;
import com.amosyo.serv.user.projects.user.domain.User;
import com.amosyo.serv.user.projects.user.exception.RegisterUserException;
import com.amosyo.serv.user.projects.user.respository.UserRepository;
import com.amosyo.serv.user.projects.user.service.UserService;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class UserServiceImpl implements UserService {

    @Resource(name = "bean/UserRepository")
    private UserRepository userRepository;

    @Resource(name = "bean/Validator")
    private Validator validator;

    @Override
    public void register(@NonNull final UserBO userBO) {
        final Set<ConstraintViolation<UserBO>> violations = validator.validate(userBO);
        if (!violations.isEmpty()) {
            throw new RegisterUserException(violations.stream().findFirst().get().getMessage());
        }
        final User user = new User();
        user.setId(Long.parseLong(userBO.getId()));
        user.setName(userBO.getName());
        user.setPassword(userBO.getPassword());
        user.setEmail(userBO.getEmail());
        user.setPhoneNumber(userBO.getPhoneNumber());
        userRepository.save(user);
    }
}

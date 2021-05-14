package com.amosyo.application.user.projects.user.respository;

import com.amosyo.application.user.projects.user.domain.User;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class DbUserRepository implements UserRepository{

    @Resource(name = "bean/EntityManager")
    private EntityManager entityManager;

    @Override
    public void save(@NonNull User user) {
        entityManager.persist(user);
//        DBConnectionManager.getInstance().save(user);
    }
}

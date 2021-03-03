package com.amosyo.serv.user.projects.user.respository;

import com.amosyo.serv.user.projects.user.domain.User;
import com.amosyo.serv.user.projects.user.sql.DBConnectionManager;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class DbUserRepository implements UserRepository{

    @Override
    public void save(@NonNull User user) {
        DBConnectionManager.getInstance().save(user);
    }
}

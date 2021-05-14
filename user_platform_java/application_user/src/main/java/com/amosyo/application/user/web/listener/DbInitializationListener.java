package com.amosyo.application.user.web.listener;

import com.amosyo.dependency.injection.ComponentContext;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class DbInitializationListener implements ServletContextListener {

    public static final String DROP_USERS_TABLE_DDL_SQL = "DROP TABLE users";

    public static final String CREATE_USERS_TABLE_DDL_SQL = "CREATE TABLE users(" +
            "id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "name VARCHAR(16) NOT NULL, " +
            "password VARCHAR(64) NOT NULL, " +
            "email VARCHAR(64) NOT NULL, " +
            "phoneNumber VARCHAR(64) NOT NULL" +
            ")";

    private static final List<String> CREATE_TABLE_DDL_SQL_S = new ArrayList<String>(1) {{
        add(CREATE_USERS_TABLE_DDL_SQL);
    }};

    @Resource(name = "bean/EntityManager")
    private EntityManager entityManager;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ComponentContext.getInstance().injectComponents(this, getClass());
        doCreateTable();
    }

    private void doCreateTable() {
        CREATE_TABLE_DDL_SQL_S.forEach(this::doExecuteSql);
    }

    private void doExecuteSql(@NonNull final String ddl) {
        final EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            entityManager.createNativeQuery(ddl).executeUpdate();
        } catch (Throwable ignore) {

        } finally {
            transaction.commit();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

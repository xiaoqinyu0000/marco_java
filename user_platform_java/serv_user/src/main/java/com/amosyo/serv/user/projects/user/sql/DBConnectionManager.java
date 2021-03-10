package com.amosyo.serv.user.projects.user.sql;

import com.amosyo.serv.user.function.ThrowableFunction;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang.ClassUtils.wrapperToPrimitive;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class DBConnectionManager {

    public static final String DROP_USERS_TABLE_DDL_SQL = "DROP TABLE users";

    public static final String CREATE_USERS_TABLE_DDL_SQL = "CREATE TABLE users(" +
            "id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "name VARCHAR(16) NOT NULL, " +
            "password VARCHAR(64) NOT NULL, " +
            "email VARCHAR(64) NOT NULL, " +
            "phoneNumber VARCHAR(64) NOT NULL" +
            ")";

    private static final Map<Class<?>, String> sPreparedStatementMethodMappings = new HashMap<Class<?>, String>() {{
        put(Array.class, "setArray");
        put(String.class, "setString");
        put(Integer.class, "setInt");
        put(Long.class, "setLong");
        put(Float.class, "setFloat");
        put(Double.class, "setDouble");
    }};

    private DBConnectionManager() {
        final Connection connection = getConnection();
        try {
            final Statement statement = connection.createStatement();
            statement.execute(DROP_USERS_TABLE_DDL_SQL);
            statement.execute(CREATE_USERS_TABLE_DDL_SQL);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    private static final class Holder {
        private static final DBConnectionManager INSTANCE = new DBConnectionManager();
    }

    public static DBConnectionManager getInstance() {
        return Holder.INSTANCE;
    }

    public Connection getConnection() {
        final String databaseURL = "jdbc:derby:db/user-platform;create=true";
        try {
            final InitialContext ctx = new InitialContext();
            final DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/UserPlatformDB");
            return ds.getConnection();
//            return DriverManager.getConnection(databaseURL);
        } catch (Exception throwables) {
            throwables.printStackTrace();
            throw new RuntimeException(throwables);
        }
    }

//    public void save(@NonNull final Object o) {
//        requireNonNull(o, "o");
//        String tableName = o.getClass().getSimpleName().toLowerCase();
//        final Table tableAnnotation = o.getClass().getAnnotation(Table.class);
//        if (!isNull(tableAnnotation) && !isBlank(tableAnnotation.tableName())) {
//            tableName = tableAnnotation.tableName();
//        }
//        try {
//            final BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass(), Object.class);
//            final PropertyDescriptor[] propertyDescriptors = Arrays.stream(beanInfo.getPropertyDescriptors())
//                    .filter(descriptor -> isNull(descriptor.getReadMethod().getAnnotation(Id.class)))
//                    .collect(Collectors.toSet())
//                    .toArray(new PropertyDescriptor[]{});
//            final StringJoiner valueJoiner = new StringJoiner(",");
//            final StringJoiner keyJoiner = new StringJoiner(",");
//            Arrays.stream(propertyDescriptors).map(it -> "?").forEach(valueJoiner::add);
//            Arrays.stream(propertyDescriptors).map(FeatureDescriptor::getDisplayName).forEach(keyJoiner::add);
//            final Object[] array = Arrays.stream(propertyDescriptors).map(descriptor -> {
//                try {
//                    return descriptor.getReadMethod().invoke(o);
//                } catch (IllegalAccessException | InvocationTargetException e) {
//                    throw new IllegalArgumentException(e);
//                }
//            }).toArray();
//            final String values = valueJoiner.toString();
//            final String keys = keyJoiner.toString();
//            String sql = String.format("INSERT INTO %s (%s)VALUES(%s)", tableName, keys, values);
//            execute(sql, t -> {
//                throw new RuntimeException(t);
//            }, array);
//        } catch (IntrospectionException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }

    private <T> T executeQuery(final @NonNull String sql,
                               final @NonNull ThrowableFunction<ResultSet, T> function,
                               final @NonNull Consumer<Throwable> exceptionHandler,
                               final @NonNull Object... args) {
        requireNonNull(sql, "sql");
        requireNonNull(function, "function");
        requireNonNull(exceptionHandler, "exceptionHandler");
        final Connection connection = getConnection();
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                final Object arg = args[i];
                final Class<?> argType = arg.getClass();
                Class<?> primitive = wrapperToPrimitive(argType);

                if (isNull(primitive)) {
                    primitive = argType;
                }

                final String methodName = sPreparedStatementMethodMappings.get(argType);
                if (isBlank(methodName)) {
                    throw new IllegalArgumentException("UnSupport ArgType :" + argType);
                }
                final Method method = PreparedStatement.class.getMethod(methodName, primitive);
                method.invoke(preparedStatement, i + 1, arg);
            }
            final ResultSet resultSet = preparedStatement.executeQuery();
            return function.apply(resultSet);
        } catch (Throwable throwables) {
            exceptionHandler.accept(throwables);
        }
        return null;
    }

    private void execute(final @NonNull String sql,
                         final @NonNull Consumer<Throwable> exceptionHandler,
                         final @NonNull Object... args) {
        requireNonNull(sql, "sql");
        requireNonNull(exceptionHandler, "exceptionHandler");
        final Connection connection = getConnection();
        System.out.println("execute sql -> " + sql + ", args -> " + Arrays.toString(args));
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                final Object arg = args[i];
                final Class<?> argType = arg.getClass();
                Class<?> primitive = wrapperToPrimitive(argType);

                if (isNull(primitive)) {
                    primitive = argType;
                }

                final String methodName = sPreparedStatementMethodMappings.get(argType);
                if (isBlank(methodName)) {
                    throw new IllegalArgumentException("UnSupport ArgType :" + argType);
                }
                final Method method = PreparedStatement.class.getMethod(methodName, int.class, primitive);
                method.invoke(preparedStatement, i + 1, arg);
            }
            preparedStatement.executeUpdate();
        } catch (Throwable throwables) {
            exceptionHandler.accept(throwables);
        }
    }
}

package com.amosyo.library.mvc.web.component;

import com.amosyo.library.mvc.function.ThrowableAction;
import com.amosyo.library.mvc.function.ThrowableCallable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class ComponentContext {

    public static final String CONTEXT_NAME = ComponentContext.class.getName();
    private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";
    private static ServletContext servletContext;
    private static final Logger logger = Logger.getLogger(CONTEXT_NAME);

    private Context envContext;
    private ClassLoader classLoader;
    private Map<String, Object> componentMap = new HashMap<>();


    public void init(ServletContext servletContext) {
        ComponentContext.servletContext = servletContext;
        servletContext.setAttribute(CONTEXT_NAME, this);
        this.classLoader = servletContext.getClassLoader();
        instantiateComponents();
        initializeComponents();
    }

    private Context getJNDIEnvContext() {
        if (null == envContext) {
            Context context = null;
            try {
                context = new InitialContext();
                envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
            } catch (NamingException e) {
                throw new RuntimeException("Failed to new InitialContext", e);
            } finally {
                doClose(context);
            }
        }
        return envContext;
    }

    private <T> T lookupFromJNDI(final @NonNull String componentName) {
        return (T) ThrowableCallable.call(() -> getJNDIEnvContext().lookup(componentName), false, null);
    }

    private void doClose(@Nullable final Context context) {
        if (!isNull(context)) {
            ThrowableAction.execute(context::close);
        }
    }


    private void instantiateComponents() {
        final Map<String, Object> componentMap = listComponentNames("/").stream().collect(toMap(it -> it, this::lookupFromJNDI, (o1, o2) -> o1));
        this.componentMap.putAll(componentMap);
    }

    public <C> C getComponent(final @NonNull String name) {
        requireNonNull(name);
        return (C) componentMap.get(name);
    }

    @NonNull
    private List<String> listComponentNames(@NonNull final String dirName) {
        return ThrowableCallable.call(() -> {
            final NamingEnumeration<NameClassPair> namingEnumeration = getJNDIEnvContext().list(dirName);
            if (isNull(namingEnumeration)) {
                return Collections.emptyList();
            }
            final List<String> fullNames = new LinkedList<>();
            while (namingEnumeration.hasMoreElements()) {
                final NameClassPair nameClassPair = namingEnumeration.nextElement();
                final String className = nameClassPair.getClassName();
                final Class<?> aClass = classLoader.loadClass(className);
                if (Context.class.isAssignableFrom(aClass)) {
                    // 目录
                    Optional.of(listComponentNames(nameClassPair.getName())).ifPresent(fullNames::addAll);
                } else {
                    final String fullName = dirName.startsWith("/") ? nameClassPair.getName() : dirName + "/" + nameClassPair.getName();
                    fullNames.add(fullName);
                }
            }
            return fullNames;
        }, true, Collections.emptyList());
    }

    /**
     * 初始化组件（支持 Java 标准 Commons Annotation 生命周期）
     * <ol>
     *  <li>注入阶段 - {@link javax.annotation.Resource}</li>
     *  <li>初始阶段 - {@link javax.annotation.PostConstruct}</li>
     *  <li>销毁阶段 - {@link javax.annotation.PreDestroy}</li>
     * </ol>
     */
    private void initializeComponents() {
        this.componentMap.values().forEach(component -> {
            Class<?> aClass = component.getClass();
            injectComponents(component, aClass);
            processPostConstruct(component, aClass);
        });
    }

    public void injectComponents(Object component, Class<?> aClass) {
        Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Resource.class))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .forEach(field -> {
                    final Resource resource = field.getAnnotation(Resource.class);
                    final Object obj = ThrowableCallable.call(() -> getComponent(resource.name()), false, null);
                    ThrowableAction.execute(() -> {
                        field.setAccessible(true);
                        field.set(component, obj);
                        field.setAccessible(false);
                    });
                });
    }

    private void processPostConstruct(Object component, Class<?> aClass) {
        Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(PostConstruct.class))
                .filter(method -> 0 == method.getParameterCount())
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .forEach(method -> ThrowableAction.execute(() -> method.invoke(component)));
    }

    public static ComponentContext getInstance() {
        return (ComponentContext) ComponentContext.servletContext.getAttribute(CONTEXT_NAME);
    }

    public void onDestroyed() {
        componentMap.values().forEach(component-> processPreDestroy(component, component.getClass()));
    }

    private void processPreDestroy(Object component, Class<?> aClass) {
        Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(PreDestroy.class))
                .filter(method -> 0 == method.getParameterCount())
                .filter(method -> !Modifier.isStatic(method.getModifiers()))
                .forEach(method -> ThrowableAction.execute(() -> method.invoke(component)));
    }
}

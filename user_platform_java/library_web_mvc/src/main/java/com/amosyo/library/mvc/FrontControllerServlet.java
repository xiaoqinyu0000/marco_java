package com.amosyo.library.mvc;

import com.amosyo.library.mvc.controller.Controller;
import com.amosyo.library.mvc.controller.PageController;
import com.amosyo.library.mvc.controller.RestController;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang.StringUtils.substringAfter;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class FrontControllerServlet extends HttpServlet {

    private static final Set<String> HTTP_METHODS_DEFAULT = new HashSet<String>() {{
        addAll(asList(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));
    }};


    private final Map<String, Controller> controllerMap = new HashMap<>();
    private final Map<String, HandlerMethodInfo> handlerMethodInfoMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        initHandlerMethods();
    }

    private void initHandlerMethods() {
        for (final Controller controller : ServiceLoader.load(Controller.class)) {
            final Class<? extends Controller> controllerClass = controller.getClass();
            final Path annotation = controllerClass.getAnnotation(Path.class);
            if (isNull(annotation)) {
                throw new IllegalArgumentException("controller must use Path annotation");
            }
            final String controllerPath = annotation.value();
            for (final Method method : controllerClass.getDeclaredMethods()) {
                if (controller instanceof RestController) {
                    if (isNull(method.getAnnotation(Path.class))) {
                        System.out.println("not match method, it is " + method.getName());
                        continue;
                    }
                }
                final Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                final String methodPath = Optional.ofNullable(method.getAnnotation(Path.class))
                        .map(Path::value)
                        .filter(StringUtils::isNotBlank)
                        .map(it -> controllerPath + it)
                        .orElse(controllerPath);
                System.out.println("add methodPath, it is " + methodPath);
                handlerMethodInfoMap.put(methodPath, new HandlerMethodInfo(methodPath, method, supportedHttpMethods));
                controllerMap.put(methodPath, controller);
            }
        }
    }


    @NonNull
    private Set<String> findSupportedHttpMethods(final @NonNull Method method) {
        final Set<String> methods = Arrays.stream(method.getAnnotations())
                .map(it -> it.annotationType().getAnnotation(HttpMethod.class))
                .filter(Objects::nonNull)
                .map(HttpMethod::value)
                .collect(toSet());
        if (methods.isEmpty()) {
            methods.addAll(HTTP_METHODS_DEFAULT);
        }
        return methods;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String requestURI = req.getRequestURI();
        final String contextPath = req.getContextPath();
        final String requestMappingPath = substringAfter(requestURI, StringUtils.replace(contextPath, "//", "/"));
        final Controller controller = controllerMap.get(requestMappingPath);
        System.out.println("controller->" + controller);
        if (isNull(controller)) {
            System.out.println("controller is null, path is " + requestMappingPath);
            super.service(req, resp);
            return;
        }
        final HandlerMethodInfo handlerMethodInfo = handlerMethodInfoMap.get(requestMappingPath);
        if (isNull(handlerMethodInfo)) {
            System.out.println("handlerMethodInfo is null, path is " + requestMappingPath);
            super.service(req, resp);
            return;
        }
        System.out.println("handlerMethodInfo->" + handlerMethodInfo.getRequestMethod());
        final String reqMethod = req.getMethod();
        if (!handlerMethodInfo.getSupportHttpMethods().contains(reqMethod)) {
            System.out.println("SC_METHOD_NOT_ALLOWED");
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        try {
            System.out.println("controller->" + controller);
            System.out.println("method->" + handlerMethodInfo.getRequestMethod());
            if (controller instanceof PageController) {
                final PageController pageController = (PageController) controller;
                String viewPath = pageController.execute(req, resp);
                if (!viewPath.startsWith("/")) {
                    viewPath = "/" + viewPath;
                }
                final ServletContext servletContext = req.getServletContext();
                final RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
                requestDispatcher.forward(req, resp);
                return;
            } else if (controller instanceof RestController) {
                System.out.println("method name=" + handlerMethodInfo.getRequestMethod().getName());
                handlerMethodInfo.getRequestMethod().invoke(controller, req, resp);
            } else {
                throw new ServletException("Un Support Controller " + controller);
            }
        } catch (Throwable throwable) {
            if (throwable.getCause() instanceof IOException) {
                throw new ServletException("Failed " + throwable);
            } else {
                throw new ServletException("Failed on FrontControllerServlet", throwable);
            }
        }

    }
}

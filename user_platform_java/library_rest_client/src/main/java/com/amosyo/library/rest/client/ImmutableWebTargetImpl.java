package com.amosyo.library.rest.client;

import org.checkerframework.checker.nullness.qual.NonNull;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class ImmutableWebTargetImpl implements WebTarget {

    private final UriBuilder uriBuilder;

    public ImmutableWebTargetImpl(@NonNull final UriBuilder uriBuilder) {
        requireNonNull(uriBuilder, "uriBuilder");
        this.uriBuilder = uriBuilder.clone();
    }

    @Override
    public URI getUri() {
        return this.uriBuilder.build();
    }

    @Override
    public UriBuilder getUriBuilder() {
        return this.uriBuilder;
    }

    @Override
    public WebTarget path(String s) {
        return new ImmutableWebTargetImpl(this.uriBuilder.path(s));
    }

    @Override
    public WebTarget resolveTemplate(String s, Object o) {
        return new ImmutableWebTargetImpl(this.uriBuilder.resolveTemplate(s, o));
    }

    @Override
    public WebTarget resolveTemplate(String s, Object o, boolean b) {
        return new ImmutableWebTargetImpl(this.uriBuilder.resolveTemplate(s, o, b));
    }

    @Override
    public WebTarget resolveTemplateFromEncoded(String s, Object o) {
        return new ImmutableWebTargetImpl(this.uriBuilder.resolveTemplateFromEncoded(s, o));
    }

    @Override
    public WebTarget resolveTemplates(Map<String, Object> map) {
        return new ImmutableWebTargetImpl(this.uriBuilder.resolveTemplates(map));
    }

    @Override
    public WebTarget resolveTemplates(Map<String, Object> map, boolean b) {
        return new ImmutableWebTargetImpl(this.uriBuilder.resolveTemplates(map, b));
    }

    @Override
    public WebTarget resolveTemplatesFromEncoded(Map<String, Object> map) {
        return new ImmutableWebTargetImpl(this.uriBuilder.resolveTemplatesFromEncoded(map));
    }

    @Override
    public WebTarget matrixParam(String s, Object... objects) {
        return new ImmutableWebTargetImpl(this.uriBuilder.matrixParam(s, objects));
    }

    @Override
    public WebTarget queryParam(String s, Object... objects) {
        return new ImmutableWebTargetImpl(this.uriBuilder.queryParam(s, objects));
    }

    @Override
    public Invocation.Builder request() {
        return new InvocationBuilderImpl(this.uriBuilder);
    }

    @Override
    public Invocation.Builder request(String... strings) {
        return this.request().accept(strings);
    }

    @Override
    public Invocation.Builder request(MediaType... mediaTypes) {
        return this.request().accept(mediaTypes);
    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public WebTarget property(String s, Object o) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> aClass) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> aClass, int i) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> aClass, Class<?>... classes) {
        return null;
    }

    @Override
    public WebTarget register(Class<?> aClass, Map<Class<?>, Integer> map) {
        return null;
    }

    @Override
    public WebTarget register(Object o) {
        return null;
    }

    @Override
    public WebTarget register(Object o, int i) {
        return null;
    }

    @Override
    public WebTarget register(Object o, Class<?>... classes) {
        return null;
    }

    @Override
    public WebTarget register(Object o, Map<Class<?>, Integer> map) {
        return null;
    }
}

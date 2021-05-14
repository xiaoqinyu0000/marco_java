package com.amosyo.library.rest.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class InvocationBuilderImpl implements Invocation.Builder {

    private final UriBuilder uriBuilder;
    private final Set<MediaType> mMediaTypes = Sets.newHashSet();
    private final Set<String> mEncodings = Sets.newHashSet();
    private Cookie mCookie = null;
    private CacheControl mCacheControl = null;
    private final Map<String, Object> mHeaderMap = Maps.newHashMap();
    private final Map<String, Object> mPropertyMap = Maps.newHashMap();

    public InvocationBuilderImpl(UriBuilder uriBuilder) {
        this.uriBuilder = requireNonNull(uriBuilder, "uriBuilder");
    }

    @Override
    public Invocation build(String method) {
        requireNonNull(method, "method");
        return this.build(method, null);
    }

    @Override
    public Invocation build(String method, Entity<?> entity) {
        requireNonNull(method, "method");
        switch (method) {
            case GET:
                return this.buildGet();
            case POST:
                return this.buildPost(entity);
            case PUT:
                return this.buildPut(entity);
            case DELETE:
                return this.buildDelete();
            default:
                break;
        }
        return null;
    }

    @Override
    public Invocation buildGet() {
        return new HttpGetInvocation(uriBuilder.build(), mHeaderMap);
    }

    @Override
    public Invocation buildDelete() {
        return null;
    }

    @Override
    public Invocation buildPost(Entity<?> entity) {
        return new HttpPostInvocation(uriBuilder.build(), requireNonNull(entity, "entity"), mHeaderMap);
    }

    @Override
    public Invocation buildPut(Entity<?> entity) {
        return null;
    }

    @Override
    public AsyncInvoker async() {
        return null;
    }

    @Override
    public Invocation.Builder accept(String... strings) {
        MediaType[] mediaTypes = Stream.of(strings).map(MediaType::valueOf).toArray(MediaType[]::new);
        return this.accept(mediaTypes);
    }

    @Override
    public Invocation.Builder accept(MediaType... mediaTypes) {
        this.mMediaTypes.addAll(Lists.newArrayList(mediaTypes));
        return this;
    }

    private final Set<Locale> mLocales = Sets.newHashSet();

    @Override
    public Invocation.Builder acceptLanguage(Locale... locales) {
        this.mLocales.addAll(Sets.newHashSet(locales));
        return this;
    }

    @Override
    public Invocation.Builder acceptLanguage(String... strings) {
        final Locale[] locales = Arrays.stream(strings).map(Locale::forLanguageTag).toArray(Locale[]::new);
        return this.acceptLanguage(locales);
    }

    @Override
    public Invocation.Builder acceptEncoding(String... strings) {
        this.mEncodings.addAll(Sets.newHashSet(strings));
        return this;
    }

    @Override
    public Invocation.Builder cookie(Cookie cookie) {
        this.mCookie = cookie;
        return this;
    }

    @Override
    public Invocation.Builder cookie(String s, String s1) {
        return this.cookie(new Cookie(s, s1));
    }

    @Override
    public Invocation.Builder cacheControl(CacheControl cacheControl) {
        this.mCacheControl = cacheControl;
        return this;
    }

    @Override
    public Invocation.Builder header(String s, Object o) {
        this.mHeaderMap.put(s, o);
        return this;
    }

    @Override
    public Invocation.Builder headers(MultivaluedMap<String, Object> multivaluedMap) {
        this.mHeaderMap.putAll(multivaluedMap);
        return this;
    }

    @Override
    public Invocation.Builder property(String s, Object o) {
        this.mPropertyMap.put(s, o);
        return this;
    }

    @Override
    public Response get() {
        return this.buildGet().invoke();
    }

    @Override
    public <T> T get(Class<T> aClass) {
        return this.buildGet().invoke(aClass);
    }

    @Override
    public <T> T get(GenericType<T> genericType) {
        return this.buildGet().invoke(genericType);
    }

    @Override
    public Response put(Entity<?> entity) {
        return this.buildPut(entity).invoke();
    }

    @Override
    public <T> T put(Entity<?> entity, Class<T> aClass) {
        return this.buildPut(entity).invoke(aClass);
    }

    @Override
    public <T> T put(Entity<?> entity, GenericType<T> genericType) {
        return this.buildPut(entity).invoke(genericType);
    }

    @Override
    public Response post(Entity<?> entity) {
        return this.buildPost(entity).invoke();
    }

    @Override
    public <T> T post(Entity<?> entity, Class<T> aClass) {
        return this.buildPost(entity).invoke(aClass);
    }

    @Override
    public <T> T post(Entity<?> entity, GenericType<T> genericType) {
        return this.buildPost(entity).invoke(genericType);
    }

    @Override
    public Response delete() {
        return this.buildDelete().invoke();
    }

    @Override
    public <T> T delete(Class<T> aClass) {
        return this.buildDelete().invoke(aClass);
    }

    @Override
    public <T> T delete(GenericType<T> genericType) {
        return this.buildDelete().invoke(genericType);
    }

    @Override
    public Response head() {
        return null;
    }

    @Override
    public Response options() {
        return null;
    }

    @Override
    public <T> T options(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T options(GenericType<T> genericType) {
        return null;
    }

    @Override
    public Response trace() {
        return null;
    }

    @Override
    public <T> T trace(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T trace(GenericType<T> genericType) {
        return null;
    }

    @Override
    public Response method(String s) {
        return this.build(s).invoke();
    }

    @Override
    public <T> T method(String s, Class<T> aClass) {
        return this.build(s).invoke(aClass);
    }

    @Override
    public <T> T method(String s, GenericType<T> genericType) {
        return this.build(s).invoke(genericType);
    }

    @Override
    public Response method(String s, Entity<?> entity) {
        return this.build(s, entity).invoke();
    }

    @Override
    public <T> T method(String s, Entity<?> entity, Class<T> aClass) {
        return this.build(s, entity).invoke(aClass);
    }

    @Override
    public <T> T method(String s, Entity<?> entity, GenericType<T> genericType) {
        return this.build(s, entity).invoke(genericType);
    }
}

package com.amosyo.library.rest.client;

import com.amosyo.library.rest.client.function.ThrowableCallable;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.Future;

import static java.util.Objects.requireNonNull;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class HttpGetInvocation implements Invocation {

    private final URI mUri;
    private final Map<String, Object> mHeaderMap;

    public HttpGetInvocation(@NonNull final URI uri, @NonNull final Map<String, Object> headerMap) {
        this.mUri = requireNonNull(uri, "uri");
        this.mHeaderMap = requireNonNull(headerMap, "headerMap");
    }

    @Override
    public Invocation property(String s, Object o) {
        return this;
    }

    @Override
    public Response invoke() {
        final OkHttpClient okHttpClient = new OkHttpClient();

        final Request request = new Request.Builder() {{
            url(ThrowableCallable.call(mUri::toURL, false, null));
            mHeaderMap.forEach((key, value) -> {
                addHeader(key, String.valueOf(value));
            });
            get();
        }}.build();

        final Call call = okHttpClient.newCall(request);
        final okhttp3.Response response = ThrowableCallable.call(call::execute, false, null);

        return new ResponseImpl(response);
    }

    @Override
    public <T> T invoke(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> T invoke(GenericType<T> genericType) {
        return null;
    }

    @Override
    public Future<Response> submit() {
        return null;
    }

    @Override
    public <T> Future<T> submit(Class<T> aClass) {
        return null;
    }

    @Override
    public <T> Future<T> submit(GenericType<T> genericType) {
        return null;
    }

    @Override
    public <T> Future<T> submit(InvocationCallback<T> invocationCallback) {
        return null;
    }
}

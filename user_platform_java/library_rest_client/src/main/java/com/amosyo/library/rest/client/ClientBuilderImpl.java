package com.amosyo.library.rest.client;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Configuration;
import java.security.KeyStore;
import java.util.Map;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class ClientBuilderImpl extends ClientBuilder {

    @Override
    public ClientBuilder withConfig(Configuration configuration) {
        return null;
    }

    @Override
    public ClientBuilder sslContext(SSLContext sslContext) {
        return null;
    }

    @Override
    public ClientBuilder keyStore(KeyStore keyStore, char[] chars) {
        return null;
    }

    @Override
    public ClientBuilder trustStore(KeyStore keyStore) {
        return null;
    }

    @Override
    public ClientBuilder hostnameVerifier(HostnameVerifier hostnameVerifier) {
        return null;
    }

    @Override
    public Client build() {
        return new ClientImpl();
    }

    @Override
    public Configuration getConfiguration() {
        return null;
    }

    @Override
    public ClientBuilder property(String s, Object o) {
        return null;
    }

    @Override
    public ClientBuilder register(Class<?> aClass) {
        return null;
    }

    @Override
    public ClientBuilder register(Class<?> aClass, int i) {
        return null;
    }

    @Override
    public ClientBuilder register(Class<?> aClass, Class<?>... classes) {
        return null;
    }

    @Override
    public ClientBuilder register(Class<?> aClass, Map<Class<?>, Integer> map) {
        return null;
    }

    @Override
    public ClientBuilder register(Object o) {
        return null;
    }

    @Override
    public ClientBuilder register(Object o, int i) {
        return null;
    }

    @Override
    public ClientBuilder register(Object o, Class<?>... classes) {
        return null;
    }

    @Override
    public ClientBuilder register(Object o, Map<Class<?>, Integer> map) {
        return null;
    }
}

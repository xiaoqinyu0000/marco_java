package com.amosyo.library.rest.client;

import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class TestClientImpl {


    @Test
    public void testGet() {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target("https://www.baidu.com")      // WebTarget
                .request() // Invocation.Builder
                .get();                                     //  Response

        String content = response.readEntity(String.class);

        System.out.println(content);
    }

    @Test
    public void testPost() {
        final Client client = ClientBuilder.newClient();

        final Entity<String> entity = Entity.text("");

        final Response response = client
                .target("https://www.baidu.com")      // WebTarget
                .request() // Invocation.Builder
                .post(entity);                                     //  Response

        final String content = response.readEntity(String.class);

        System.out.println(content);
    }

}

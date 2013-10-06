package org.reefy.test;

import org.joda.time.Duration;
import org.junit.Assert;
import org.junit.Test;
import org.reefy.transportrest.api.*;
import org.reefy.transportrest.api.transport.Contact;
import org.reefy.transportrest.api.transport.TransportClient;
import org.reefy.transportrest.api.transport.TransportException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public abstract class AbstractIntegrationTest<C extends Contact> {

    private final TransportFactory<C> transportFactory;
    private final AppFactory appFactory;

    protected AbstractIntegrationTest(TransportFactory<C> transportFactory, AppFactory appFactory) {
        this.transportFactory = transportFactory;
        this.appFactory = appFactory;
    }

    @Test
    public void testPutGet() {
        final TransportClient<C> client = transportFactory.buildClient();
        final TransportFactory.ServerWhatever<C> serverWhatever =
            transportFactory.buildServer(null);

        final AppClient appClient = appFactory.buildClient();

        // Just use a random key and constant value
        final Key key = RawKey.pseudorandom();
        final String message = "test";
        final Value value = new RawValue(message.getBytes());

        appClient.put(key, value, Duration.standardHours(1), new AppClient.PutCallback() {
            @Override
            public void succeed() {

            }

            @Override
            public void fail(Throwable e) {
                Assert.fail(e.getMessage());
            }
        });
    }
}

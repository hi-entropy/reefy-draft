package org.reefy.samplechat;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractIdleService;

import org.reefy.transportrest.api.*;
import org.reefy.transportrest.api.transport.TransportClient;
import org.reefy.transportrest.api.transport.local.LocalContact;
import org.reefy.transportrest.api.transport.local.LocalTransportClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentMap;

import static org.joda.time.Duration.standardMinutes;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class HelloWorldClient extends AbstractIdleService {

    public static void main(String[] args) throws IOException {
        ConcurrentMap<LocalContact, AppServer> contactsToServers = Maps.newConcurrentMap();
        final TransportClient transportClient = new LocalTransportClient(contactsToServers);
        final SimpleAppClient simpleAppClient = new SimpleAppClient(transportClient);
        final HelloWorldClient helloWorldClient = new HelloWorldClient(simpleAppClient);

        // Just use a random key
        final Key key = RawKey.pseudorandom();

        // The value is a short message
        System.out.print("Enter your name: ");
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        final String message = "Hello world, my name is " + bufferedReader.readLine();
        final Value value = new RawValue(message.getBytes());

        // Put the key/value pair into Reefy
        helloWorldClient.getAppClient().put(key, value, standardMinutes(10), new SimpleAppClient.PutCallback() {
            @Override
            public void succeed() {
                System.out.println("Hello world message uploaded.");
            }

            @Override
            public void fail(Throwable e) {
                System.out.println("Hello world message failed to upload: " + e.getMessage());
            }
        });
    }

    private final SimpleAppClient appClient;

    public HelloWorldClient(SimpleAppClient appClient) {
        this.appClient = appClient;
    }

    @Override
    protected void startUp() throws Exception {
        appClient.startAndWait();

        System.out.println("Starting up...");
    }

    @Override
    protected void shutDown() throws Exception {
        appClient.stopAndWait();
    }

    public SimpleAppClient getAppClient() {
        return appClient;
    }
}

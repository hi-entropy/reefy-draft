package org.reefy.helloworld;

import org.reefy.transportrest.RestContact;
import org.reefy.transportrest.RestTransportClient;
import org.reefy.transportrest.api.*;
import org.reefy.transportrest.api.transport.TransportClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.joda.time.Duration.standardMinutes;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class HelloWorldClient {

    public static void main(String[] args) throws IOException {
        final TransportClient transportClient = new RestTransportClient();
        final SimpleAppClient appClient = new SimpleAppClient(transportClient);

        // TODO: This should NOT be null... we need a way to bootstrap by adding just an IP and port, w/o a key
        appClient.addContact(new RestContact(null, "localhost", 8000));

        appClient.startAndWait();

        System.out.println("Starting up...");

        // Just use a random key
        final Key key = RawKey.pseudorandom();

        // The value is a short message
        System.out.print("Enter your name: ");
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        final String message = "Hello world, my name is " + bufferedReader.readLine();
        final Value value = new RawValue(message.getBytes(StandardCharsets.UTF_8));

        // Put the key/value pair into Reefy
        appClient.put(key, value, standardMinutes(10), new SimpleAppClient.PutCallback() {
            @Override
            public void succeed() {
                System.out.println("Message uploaded.");

                appClient.get(key, new AppClient.GetCallback() {
                    @Override
                    public void succeed(Value value) {
                        final String valueString = new String(value.getBytes(), StandardCharsets.UTF_8);
                        System.out.println("Message downloaded. Text is: " + valueString);
                        appClient.stopAndWait();
                    }

                    @Override
                    public void notFound() {
                        System.err.println("Hello world message was not found.");
                        appClient.stopAndWait();
                    }

                    @Override
                    public void fail(Throwable e) {
                        System.err.println("Hello world message failed to download: " + getStackTrace(e));
                        appClient.stopAndWait();
                    }
                });
            }

            @Override
            public void fail(Throwable e) {
                System.out.println("Hello world message failed to upload: " + getStackTrace(e));
                appClient.stopAndWait();
            }
        });
    }
}

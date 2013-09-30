package org.reefy.samplechat;

import com.google.common.util.concurrent.AbstractIdleService;

import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.RawValue;
import org.reefy.transportrest.api.SimpleAppClient;
import org.reefy.transportrest.api.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Paul Kernfeld - pk@knewton.com
 */
public class HelloWorldClient extends AbstractIdleService {

    public static void main(int[] args) throws IOException {
        final HelloWorldClient helloWorldClient = new HelloWorldClient(client);

        // Just use a random key
        final Key key = RawKey.pseudorandom();

        // The value is a short message
        System.out.print("Enter your name: ");
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        final String message = "Hello world, my name is " + bufferedReader.readLine();
        final Value value = new RawValue(message.getBytes());

        // Put the key/value pair into Reefy
        helloWorldClient.put(key, value, );
    }

    private final SimpleAppClient client;

    public HelloWorldClient(SimpleAppClient client) {
        this.client = client;
    }

    @Override
    protected void startUp() throws Exception {
        client.startAndWait();

        System.out.println("Starting up...");
    }

    @Override
    protected void shutDown() throws Exception {
        client.stopAndWait();
    }
}

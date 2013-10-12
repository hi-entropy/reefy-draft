package org.reefy.transportrest;

import org.reefy.test.AbstractTransportServerWhatever;
import org.reefy.test.TransportFactory;
import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.transport.TransportClient;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class RestTransportFactory implements TransportFactory<RestContact> {
    // TODO: eww, use DI
    private static final AtomicInteger currentPort = new AtomicInteger(8000);

    @Override
    public ServerWhatever<RestContact> buildServer(AppServerHandler handler) {
        final Key key = RawKey.pseudorandom();
        final RestContact contact = new RestContact(
            key,
            "localhost",
            currentPort.getAndIncrement()
        );
        final RestTransportServer server = new RestTransportServer(contact, handler);
        return new AbstractTransportServerWhatever<RestContact>(contact, server);
    }

    @Override
    public TransportClient<RestContact> buildClient() {
        return new RestTransportClient();
    }

    @Override
    public RestContact buildMockContact() {
        return new RestContact(
            RawKey.pseudorandom(),
            Integer.toString(new Random().nextInt()),
            new Random().nextInt()
        );
    }
}

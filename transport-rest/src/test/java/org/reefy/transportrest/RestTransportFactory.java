package org.reefy.transportrest;

import org.reefy.core.AppServerHandler;
import org.reefy.core.Key;
import org.reefy.core.RawKey;
import org.reefy.core.transport.TransportClient;
import org.reefy.core.transport.test.AbstractTransportServerWhatever;
import org.reefy.core.transport.test.TransportFactory;

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

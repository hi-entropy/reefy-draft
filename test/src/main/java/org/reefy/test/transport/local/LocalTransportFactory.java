package org.reefy.test.transport.local;

import com.google.common.collect.Maps;
import org.reefy.test.transport.AbstractTransportServerWhatever;
import org.reefy.test.transport.TransportFactory;
import org.reefy.transportrest.api.AppServerHandler;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.transport.TransportClient;
import org.reefy.transportrest.api.transport.local.LocalContact;
import org.reefy.transportrest.api.transport.local.LocalTransportClient;
import org.reefy.transportrest.api.transport.local.LocalTransportServer;

import java.util.concurrent.ConcurrentMap;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class LocalTransportFactory implements TransportFactory<LocalContact> {
    private final ConcurrentMap<LocalContact, LocalTransportServer> contactsToServers =
        Maps.newConcurrentMap();

    @Override
    public ServerWhatever<LocalContact> buildServer(AppServerHandler handler) {
        final Key key = RawKey.pseudorandom();
        final LocalContact contact = new LocalContact(key);
        final LocalTransportServer server = new LocalTransportServer(handler);
        contactsToServers.put(contact, server);
        return new AbstractTransportServerWhatever<LocalContact>(contact, server);
    }

    @Override
    public TransportClient<LocalContact> buildClient() {
        return new LocalTransportClient(contactsToServers);
    }

    @Override
    public LocalContact buildMockContact() {
        return new LocalContact(RawKey.pseudorandom());
    }
}

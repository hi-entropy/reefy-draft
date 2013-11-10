package org.reefy.core.transport.local;

import com.google.common.collect.Maps;

import org.reefy.core.AppServerHandler;
import org.reefy.core.Key;
import org.reefy.core.RawKey;
import org.reefy.core.transport.test.AbstractTransportServerWhatever;
import org.reefy.core.transport.TransportClient;
import org.reefy.core.transport.test.TransportFactory;

import java.util.concurrent.ConcurrentMap;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class LocalTransportFactory implements TransportFactory<LocalContact> {
    private final ConcurrentMap<LocalContact, LocalTransportServer> contactsToServers =
        Maps.newConcurrentMap();

    @Override
    public TransportFactory.ServerWhatever<LocalContact> buildServer(AppServerHandler handler) {
        final Key key = RawKey.pseudorandom();
        final LocalContact contact = new LocalContact(key);
        final LocalTransportServer server = new LocalTransportServer(contact, handler);
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

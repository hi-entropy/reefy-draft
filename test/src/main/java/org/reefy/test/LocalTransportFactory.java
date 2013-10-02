package org.reefy.test;

import com.google.common.collect.Maps;
import org.reefy.transportrest.api.AppServer;
import org.reefy.transportrest.api.Key;
import org.reefy.transportrest.api.RawKey;
import org.reefy.transportrest.api.TransportServer;
import org.reefy.transportrest.api.transport.TransportClient;
import org.reefy.transportrest.api.transport.local.LocalContact;
import org.reefy.transportrest.api.transport.local.LocalTransportClient;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class LocalTransportFactory implements TransportFactory<LocalContact> {
    private final ConcurrentMap<LocalContact, AppServer> contactsToServers = Maps.newConcurrentMap();

    @Override
    public ServerWhatever<LocalContact> buildServer() {
        final Key key = RawKey.pseudorandom();
        final LocalContact contact = new LocalContact(key);
        final LocalTransportServer server = new LocalServer
        return new AbstractTransportServerWhatever<LocalContact>(contact);
    }

    @Override
    public TransportClient<LocalContact> buildClient() {
        return new LocalTransportClient(contactsToServers);
    }
}

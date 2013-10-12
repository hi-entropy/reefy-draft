package org.reefy.test;

import org.reefy.transportrest.api.transport.TransportServer;
import org.reefy.transportrest.api.transport.Contact;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class AbstractTransportServerWhatever<C extends Contact>
    implements TransportFactory.ServerWhatever<C> {
    private final C contact;
    private final TransportServer<C> server;

    public AbstractTransportServerWhatever(C contact, TransportServer<C> server) {
        this.contact = contact;
        this.server = server;
    }

    public C getContact() {
        return contact;
    }

    public TransportServer<C> getServer() {
        return server;
    }
}

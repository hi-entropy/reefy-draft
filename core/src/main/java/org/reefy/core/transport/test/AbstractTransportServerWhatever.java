package org.reefy.core.transport.test;

import org.reefy.core.transport.TransportServer;
import org.reefy.core.transport.Contact;

/**
 * @author Paul Kernfeld <hi-entropy@gmail.com>
 */
public class AbstractTransportServerWhatever<C extends Contact>
    implements TransportFactory.Serv1erWhatever<C> {
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
